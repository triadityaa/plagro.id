package com.adit.bangkit.plagroid.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.adit.bangkit.plagroid.R
import com.adit.bangkit.plagroid.databinding.ActivityCheckoutBinding
import com.adit.bangkit.plagroid.firestore.FirestoreClass
import com.adit.bangkit.plagroid.models.*
import com.adit.bangkit.plagroid.models.Address
import com.adit.bangkit.plagroid.ui.adapters.CartItemsListAdapter
import com.adit.bangkit.plagroid.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.midtrans.sdk.corekit.core.MidtransSDK
import com.midtrans.sdk.corekit.core.TransactionRequest
import com.midtrans.sdk.corekit.core.themes.CustomColorTheme
import com.midtrans.sdk.corekit.models.BillingAddress
import com.midtrans.sdk.corekit.models.CustomerDetails
import com.midtrans.sdk.corekit.models.ItemDetails
import com.midtrans.sdk.corekit.models.ShippingAddress
import com.midtrans.sdk.uikit.SdkUIFlowBuilder
import java.text.SimpleDateFormat
import java.util.*
import javax.mail.*
import javax.mail.internet.AddressException
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


/**
 * A CheckOut activity screen.
 */
class CheckoutActivity : BaseActivity() {

    //A global variable for user details
    private lateinit var mUserDetails: User

    // A global variable for the selected address details.
    private var mAddressDetails: Address? = null

    // A global variable for the product list.
    private lateinit var mProductsList: ArrayList<Product>

    // A global variable for the cart list.
    private lateinit var mCartItemsList: ArrayList<Cart>

    // A global variable for the SubTotal Amount.
    private var mSubTotal: Int = 0

    // A global variable for the Total Amount.
    private var mTotalAmount: Int = 0

    // A global variable for Order details.
    private lateinit var mOrderDetails: Order

    private lateinit var binding: ActivityCheckoutBinding

    /**
     * This function is auto created by Android when the Activity Class is created.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        // This is used to align the xml view to this class
        setContentView(binding.root)

        setupActionBar()


        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)) {
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!
        }

        if (mAddressDetails != null) {
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text =
                "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()) {
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvCheckoutMobileNumber.text = mAddressDetails?.mobileNumber
        }

        SdkUIFlowBuilder.init()
            .setClientKey(Constants.CLIENT_KEY) // client_key is mandatory
            .setContext(applicationContext) // context is mandatory
            .setTransactionFinishedCallback { result ->
                if (result.status == Constants.STATUS_PENDING) {
                    hideProgressDialog()

                    Toast.makeText(
                        this@CheckoutActivity, "Your order placed successfully.\n " +
                                "Please select your payment method", Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

            }
            // Handle finished transaction here.
            // set transaction finish callback (sdk callback)
            .setMerchantBaseUrl("https://plagroid.samuraibalifarm.com/index.php/") //set merchant url (required)
            .enableLog(true) // enable sdk log (optional)
            .setColorTheme(CustomColorTheme("#81D742", "#419845", "#61CE70"))
            .setLanguage("id") //`en` for English and `id` for Bahasa
            .buildSDK()

        binding.btnPlaceOrder.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    101
                )
            }

            Log.e("totalHarga", mTotalAmount.toString())
            val transactionRequest = TransactionRequest(
                "PLAGRO.ID-" + System.currentTimeMillis().toString(),
                mTotalAmount.toDouble()
            )
            val itemDetails = ArrayList<ItemDetails>()
            for (mCartItemsList in mCartItemsList) {
                val detail = ItemDetails(
                    mCartItemsList.product_id,
                    mCartItemsList.price.toDouble(),
                    mCartItemsList.cart_quantity,
                    mCartItemsList.title
                )
                itemDetails.add(detail)
            }
            val shippingDetails = ItemDetails("Shipping", 10000.0, 1, "PLAGRO")
            itemDetails.add(shippingDetails)
            uiKitDetails(transactionRequest, mUserDetails)
            transactionRequest.itemDetails = itemDetails
            MidtransSDK.getInstance().transactionRequest = transactionRequest
            MidtransSDK.getInstance().startPaymentUiFlow(this)

            placeAnOrder()
            updateSoldProduct(mCartItemsList, mOrderDetails)
            mAddressDetails?.let { it1 -> sendEmail(mUserDetails, mOrderDetails, it1) }
            mAddressDetails?.let { address -> sendEmailToAdmin(mOrderDetails, address)}
        }

        getProductList()
        getUserDetails()
    }


    // a function to get user details from firebase in order to send data to midtrans
    fun uiKitDetails(transactionRequest: TransactionRequest, user: User) {
        hideProgressDialog()
        mUserDetails = user
        val customerDetails = CustomerDetails()
        customerDetails.customerIdentifier = user.id
        customerDetails.phone = user.mobile.toString()
        customerDetails.firstName = user.firstName
        customerDetails.lastName = user.lastName
        customerDetails.email = user.email

        val shippingAddress = ShippingAddress()
        shippingAddress.address = mAddressDetails?.address
        shippingAddress.city = (mAddressDetails?.latlng?.plus(mAddressDetails?.latlng!!)).toString()
        shippingAddress.postalCode = mAddressDetails?.zipCode
        customerDetails.shippingAddress = shippingAddress

        val billingAddress = BillingAddress()
        billingAddress.address = mAddressDetails?.address
        billingAddress.city = (mAddressDetails?.latlng?.plus(mAddressDetails?.latlng!!)).toString()
        billingAddress.postalCode = mAddressDetails?.zipCode
        customerDetails.billingAddress = billingAddress

        transactionRequest.customerDetails = customerDetails
    }

    private fun getUserDetails() {

        // Show the progress dialog
//        showProgressDialog(resources.getString(R.string.please_wait))

        // Call the function of Firestore class to get the user details from firestore which is already created.
        FirestoreClass().getUserDetails(this@CheckoutActivity)
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to get product list to compare the current stock with the cart items.
     */
    private fun getProductList() {

        // Show the progress dialog.
        showProgressDialog()

        FirestoreClass().getAllProductsList(this@CheckoutActivity)
    }

    /**
     * A function to get the success result of product list.
     *
     * @param productsList
     */
    fun successProductsListFromFireStore(productsList: ArrayList<Product>) {

        mProductsList = productsList

        getCartItemsList()
    }

    /**
     * A function to get the list of cart items in the activity.
     */
    private fun getCartItemsList() {

        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    /**
     * A function to notify the success result of the cart items list from cloud firestore.
     *
     * @param cartList
     */
    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<Cart>) {

        // Hide progress dialog.
        hideProgressDialog()

        for (product in mProductsList) {
            for (cart in cartList) {
                if (product.product_id == cart.product_id) {
                    cart.stock_quantity = product.stock_quantity.toInt()
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this@CheckoutActivity, mCartItemsList, false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList) {

            val availableQuantity = item.stock_quantity

            if (availableQuantity > 0) {
                val price = item.price
                val quantity = item.cart_quantity

                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "Rp.$mSubTotal"
        // Here we have kept Shipping Charge is fixed as $10 but in your case it may cary. Also, it depends on the location and total amount.
        binding.tvCheckoutShippingCharge.text = "Rp.10.000"

        if (mSubTotal > 0) {
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE

            mTotalAmount = mSubTotal + 10000
            binding.tvCheckoutTotalAmount.text = "Rp$mTotalAmount"
        } else {
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }
    }

    /**
     * A function to prepare the Order details to place an order.
     */
    private fun placeAnOrder() {

        // Show the progress dialog.
        showProgressDialog()

        mOrderDetails = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails!!,
            "PLAGRO.ID-${System.currentTimeMillis()}",
            mCartItemsList[0].image,
            mSubTotal,
            10000, // The Shipping Charge is fixed as $10 for now in this case.
            mTotalAmount,
            Date().time,
            Order().id,
            Order().admin_id,
        )

        FirestoreClass().placeOrder(this@CheckoutActivity, mOrderDetails)
    }

    /**
     * A function to notify the success result of the order placed.
     */
    fun orderPlacedSuccess() {

        FirestoreClass().updateAllDetails(this@CheckoutActivity, mCartItemsList, mOrderDetails)
    }

    /**
     * A function to notify the success result after updating all the required details.
     */
    fun allDetailsUpdatedSuccessfully() {

        // Hide the progress dialog.
        hideProgressDialog()

        Toast.makeText(
            this@CheckoutActivity, "Your order placed successfully.\n " +
                    "Please select your payment method", Toast.LENGTH_SHORT
        )
            .show()

//        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//        finish()
    }

    fun updateSoldProduct(cartList: ArrayList<Cart>, order: Order) {
        val mFireStore = FirebaseFirestore.getInstance()
        val writeBatch = mFireStore.batch()

        // Prepare the sold product details
        for (cart in cartList) {

            val soldProduct = SoldProduct(
                FirestoreClass().getCurrentUserID(),
                cart.title,
                cart.price,
                cart.cart_quantity,
                cart.image,
                order.title,
                order.order_datetime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address,
                order.id,
                order.admin_id
            )

            val documentReference = mFireStore.collection(Constants.SOLD_PRODUCTS)
                .document()
            writeBatch.set(documentReference, soldProduct)
        }
    }



    private fun sendEmail(user: User, order: Order, address: Address) {
        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = order.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        try {
            val stringSenderEmail = Constants.EMAIL_SENDER
            val stringReceiverEmail = user.email
            val stringPasswordSenderEmail = Constants.EMAIL_PASSWORD
            val stringHost = "smtp.gmail.com"
            val properties = System.getProperties()
            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                }
            })
            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail))
            mimeMessage.subject = "Subject: Order PLAGRO.ID Atas Nama ${user.firstName} ${user.lastName}"
            mimeMessage.setText("Hai ${user.firstName} ${user.lastName},\n\n" +
                "Berikut ini adalah detail order anda:\n\n" +
                "Order ID: PLAGRO.ID-" + System.currentTimeMillis().toString() + "\n" +
                "Waktu Pemesanan: ${orderDateTime}\n" +
                "Sub Total: Rp. ${order.sub_total_amount}\n" +
                "Biaya Pengiriman: Rp. ${order.shipping_charge}\n" +
                "Total Biaya: Rp. ${order.total_amount}\n" +
                "Alamat Pengiriman: ${address.address}\n\n" +
                "Terima kasih telah berbelanja di PLAGRO.ID.\n\n" +
                "Salam,\n" +
                "PLAGRO.ID")
            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }

    private fun sendEmailToAdmin(order: Order, address: Address) {

        // Date Format in which the date will be displayed in the UI.
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = order.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        try {
            val stringSenderEmail = Constants.EMAIL_SENDER
            val stringReceiverEmail = Constants.EMAIL_ADMIN
            val stringPasswordSenderEmail = Constants.EMAIL_PASSWORD
            val stringHost = "smtp.gmail.com"
            val properties = System.getProperties()
            properties["mail.smtp.host"] = stringHost
            properties["mail.smtp.port"] = "465"
            properties["mail.smtp.ssl.enable"] = "true"
            properties["mail.smtp.auth"] = "true"
            val session = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail)
                }
            })
            val mimeMessage = MimeMessage(session)
            mimeMessage.addRecipient(Message.RecipientType.TO, InternetAddress(stringReceiverEmail))
            mimeMessage.subject = "Subject: Order Baru dari aplikasi E-commerce PLAGRO.ID"
            mimeMessage.setText("Hai Admin PLAGRO!,\n\n" +
                    "Berikut ini adalah detail orderan baru dari aplikasi E-commerce anda:\n\n" +
                    "Order ID: PLAGRO.ID-" + System.currentTimeMillis().toString() + "\n" +
                    "Nama Penerima: ${address.name}\n" +
                    "Waktu Pemesanan: $orderDateTime\n" +
                    "Sub Total: Rp. ${order.sub_total_amount}\n" +
                    "Biaya Pengiriman: Rp. ${order.shipping_charge}\n" +
                    "Total Biaya: Rp. ${order.total_amount}\n" +
                    "Alamat Pengiriman: ${address.address}\n\n" +
                    "Segera kemas dan kirimkan pesanan\n\n" +
                    "Salam,\n" +
                    "PLAGRO.ID")
            val thread = Thread {
                try {
                    Transport.send(mimeMessage)
                } catch (e: MessagingException) {
                    e.printStackTrace()
                }
            }
            thread.start()
        } catch (e: AddressException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        }
    }
}