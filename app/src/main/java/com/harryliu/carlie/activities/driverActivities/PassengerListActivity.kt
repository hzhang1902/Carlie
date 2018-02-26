package com.harryliu.carlie.activities.driverActivities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.harryliu.carlie.DragDelItem
import com.harryliu.carlie.R
import com.harryliu.carlie.Trip
import com.harryliu.carlie.services.AuthenticationService
import com.harryliu.carlie.services.DatabaseService
import kotlinx.android.synthetic.main.activity_passenger_list.*
import java.util.*

/**
 * @author Haofan Zhang
 *
 * @version Feb 23, 2018
 */
class PassengerListActivity : AppCompatActivity() {

    private val mTripList = ArrayList<Trip>()
    private var mListView: ListView? = null
    private val mActivity = this
    private val mAdapter: AppAdapter = AppAdapter(mTripList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_passenger_list)

        val mButtonLogout: Button = passenger_list_logout

        mButtonLogout.setOnClickListener { _ ->
            AuthenticationService.logOut(mActivity)
            mActivity.finish()
        }

        mListView = passenger_list_view
        mListView!!.adapter = mAdapter

        DatabaseService.bindTripList(::updatePassengerList)
    }

    private fun updatePassengerList(tripSnap: DataSnapshot?, mode: Int) {
        if (tripSnap != null) {
            val trip = tripSnap.getValue(Trip::class.java)
            if (trip != null) {
                when (mode) {
                    DatabaseService.ADD -> {
                        mTripList.add(trip)
                        mAdapter.notifyDataSetChanged()
                    }

                    DatabaseService.REMOVE -> {
                        for (t in mTripList) {
                            if (t.uid == trip.uid) {
                                mTripList.remove(t)
                                break
                            }
                        }
                        mAdapter.notifyDataSetChanged()
                    }
                    DatabaseService.CHANGE -> {

                    }
                }
            }
        }
    }

    internal inner class AppAdapter constructor(val mAppList: List<Trip>) : BaseAdapter() {
        override fun getCount(): Int {
            return mAppList.size
        }

        override fun getItem(position: Int): Trip {
            return mAppList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, cv: View?, parent: ViewGroup): View {

            val holder: ViewHolder
            val menuView: View
            val contentView: View
            val convertView: View
            if (cv == null) {
                contentView = View.inflate(mActivity, R.layout.swipe_content, null)
                menuView = View.inflate(mActivity, R.layout.swipe_menu, null)
                convertView = DragDelItem(contentView, menuView)
                holder = ViewHolder(convertView)
            } else {
                convertView = cv
                holder = convertView.tag as ViewHolder
            }
            val item = getItem(position)

            // change based on item
            holder.name.text = item.passenger.name
            return convertView
        }

        internal inner class ViewHolder(view: View) {
            var name: TextView
            var tv_open: TextView
            var tv_del: TextView

            init {
                // set up reference
                name = view.findViewById(R.id.passenger_list_name)
                tv_open = view.findViewById(R.id.tv_open)
                tv_del = view.findViewById(R.id.tv_del)
                view.tag = this
            }
        }
    }


}