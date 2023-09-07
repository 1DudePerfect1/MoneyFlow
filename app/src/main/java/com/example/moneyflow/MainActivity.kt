package com.example.moneyflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.moneyflow.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var deletedTransaction: Transaction
    private lateinit var transactions : List<Transaction>
    private lateinit var oldTransactions : List<Transaction>
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var linearLayoutMaanger : LinearLayoutManager
    private lateinit var db : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        transactions = arrayListOf()
        transactionAdapter = TransactionAdapter(transactions)
        linearLayoutMaanger = LinearLayoutManager(this)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "transactions").build()
        recycleview.apply {
            adapter = transactionAdapter
            layoutManager = linearLayoutMaanger
        }
        //swipe
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deleteTransaction(transactions[viewHolder.adapterPosition])
            }

        }

        val swipeHelper = ItemTouchHelper(itemTouchHelper)
        swipeHelper.attachToRecyclerView(recycleview)

        addBtn.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java )
            startActivity(intent)
        }
    }



    private fun fetchAll(){
        GlobalScope.launch {

            transactions = db.transactionDao().getAll()
        }
        runOnUiThread{
            updateDashboard()
            transactionAdapter.setData(transactions)
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
    private fun deleteTransaction(transaction: Transaction){
        deletedTransaction = transaction
        oldTransactions = transactions

        GlobalScope.launch {
            db.transactionDao().delete(transaction)
            transactions = transactions.filter { it.id != transaction.id }
            runOnUiThread{
                updateDashboard()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchAll()
    }
}
