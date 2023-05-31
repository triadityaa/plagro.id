package com.plagro.id.admin.ui.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.plagro.id.admin.R
import com.plagro.id.admin.databinding.FragmentDashboardBinding
import com.plagro.id.admin.firestore.FirestoreClass
import com.plagro.id.admin.models.Order
import com.plagro.id.admin.ui.activities.SettingsActivity
import com.plagro.id.admin.ui.activities.SoldReportActivity
import java.text.SimpleDateFormat

class DashboardFragment : BaseFragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        // If we want to use the option menu in fragment we need to add it.
        binding.chart.setOnClickListener {
            val intent = Intent(context, SoldReportActivity::class.java)
            startActivity(intent)
        }

        binding.titleReport.setOnClickListener {
            val intent = Intent(context, SoldReportActivity::class.java)
            startActivity(intent)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trsChartFormat()

        binding.chart.setOnClickListener {
            val intent = Intent(context, SoldReportActivity::class.java)
            startActivity(intent)
        }

        binding.titleReport.setOnClickListener {
            val intent = Intent(context, SoldReportActivity::class.java)
            startActivity(intent)
        }
    }

    fun showChartData(listOrder: List<Order>) {
        val sumOfData = listOrder.distinctBy { it.order_datetime }
        sumOfData.forEach { order ->
            order.total_amount = listOrder.filter { it.order_datetime == order.order_datetime }.sumOf { it.total_amount }
        }
        val allPenjualan = ArrayList<Entry>()
        val label = ArrayList<String>()
        if (sumOfData.size == 1){
            allPenjualan.add(Entry(0f, 0f))
            label.add("0")
            allPenjualan.add(Entry(1f, sumOfData[0].total_amount.toFloat()))
            label.add(SimpleDateFormat("dd/MM").format(sumOfData[0].order_datetime))
        }else {
            for (value in sumOfData) {
                val i = sumOfData.indexOf(value)
                allPenjualan.add(Entry(i.toFloat(), value.total_amount.toFloat()))
                label.add(SimpleDateFormat("dd/MM").format(value.order_datetime))
            }
        }
        val lineDataSet = LineDataSet(allPenjualan.takeLast(5), "Grafik Penjualan").apply {
            valueTextColor = Color.BLACK
            valueTextSize = 11f
            lineWidth = 2f
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
            color = Color.rgb(0, 124, 190)
            setDrawCircles(true)
            setDrawCircleHole(false)
            setCircleColor(Color.BLACK)
            circleRadius = 3f
            setDrawFilled(true)
            formLineWidth = 1f
            formSize = 15f
            fillColor = Color.rgb(0, 124, 190)
            fillAlpha = 100
        }
        with(binding.chart){
            notifyDataSetChanged()
            data = LineData(lineDataSet)
            invalidate()
            xAxis.valueFormatter = IndexAxisValueFormatter(label)
        }
        hideProgressDialog()
    }

    private fun trsChartFormat() {
        FirestoreClass().getAllOrders(this@DashboardFragment)
        with(binding.chart){
            setExtraOffsets(30f,20f,30f,20f)
            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                setDrawLabels(true)
                axisLineColor = Color.BLACK
                textColor = Color.BLACK
                setDrawGridLines(true)
                granularity = 1f
                setCenterAxisLabels(false)
            }
            axisLeft.apply {
                setDrawLabels(false)
                axisLineColor = Color.BLACK
                setDrawGridLines(true)
            }
            axisRight.apply {
                setDrawLabels(false)
                axisLineColor = Color.BLACK
                setDrawGridLines(false)
            }
            setPinchZoom(false)
            description.isEnabled = false
            legend.isEnabled = false
            isClickable = false
            setTouchEnabled(false)//custom marker tapi males
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.action_settings -> {
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        getChartData()
//        getDashboardItemsList()
    }

    private fun getChartData(){
        showProgressDialog()

        FirestoreClass().getAllOrders(this@DashboardFragment)
    }

//    private fun getDashboardItemsList() {
//        // Show the progress dialog.
//        showProgressDialog()
//
//        FirestoreClass().getDashboardItemsList(this@DashboardFragment)
//    }
//
//    /**
//     * A function to get the success result of the dashboard items from cloud firestore.
//     *
//     * @param dashboardItemsList
//     */
//    fun successDashboardItemsList(dashboardItemsList: ArrayList<Product>) {
//
//        // Hide the progress dialog.
//        hideProgressDialog()
//
//        if (dashboardItemsList.size > 0) {
//
//            binding.rvDashboardItems.visibility = View.VISIBLE
//            binding.tvNoDashboardItemsFound.visibility = View.GONE
//
//            binding.rvDashboardItems.layoutManager = GridLayoutManager(activity, 2)
//            binding.rvDashboardItems.setHasFixedSize(true)
//
//            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
//            binding.rvDashboardItems.adapter = adapter
//
//            adapter.setOnClickListener(object :
//                DashboardItemsListAdapter.OnClickListener {
//                override fun onClick(position: Int, product: Product) {
//
//                    val intent = Intent(context, ProductDetailsActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
//                    intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
//                    startActivity(intent)
//                }
//            })
//            // END
//        } else {
//            binding.rvDashboardItems.visibility = View.GONE
//            binding.tvNoDashboardItemsFound.visibility = View.VISIBLE
//        }
//    }
}