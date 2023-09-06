package com.example.moneyflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moneyflow.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var transactions : ArrayList<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutMaanger : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = arrayListOf(
            Transaction("Weekend budget", 400.00),
            Transaction("Bananas", -4.00),
            Transaction("Gasoline", -40.90),
            Transaction("Breakfast", -9.99),
            Transaction("Water bottles", -4.00),
            Transaction("Suncream", -8.00),
            Transaction("Car park", -15.00)

        )
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutMaanger = LinearLayoutManager(this)
        recycleview.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutMaanger
        }
    }
        private fun updateDashboard(){
            val totalAmount = transactions.map { it.amount }.sum()
            val budgetAmount = transactions.filter { it.amount>0 }.map { it.amount }.sum()
            val expenseAmount = totalAmount - budgetAmount
            balance.text = "$ %.2f".format(totalAmount)
            budgettext.text = "$ %.2f".format(budgetAmount)
            expendstext.text = "$ %.2f".format(expenseAmount)
        }
}
