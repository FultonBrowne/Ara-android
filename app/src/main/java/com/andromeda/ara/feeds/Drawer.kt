
/*
 * Copyright (c) 2020. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.feeds

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.RecyclerView
import com.andromeda.ara.R
import com.andromeda.ara.activitys.MainActivity
import com.mikepenz.materialdrawer.widget.AccountHeaderView
import com.andromeda.ara.client.iot.Actions
import com.andromeda.ara.client.models.FeedModel
import com.andromeda.ara.client.reminders.Reminders
import com.andromeda.ara.client.util.User
import com.andromeda.ara.constants.DrawerModeConstants
import com.andromeda.ara.feeds.Drawer.Companion.newItem
import com.andromeda.ara.phoneData.CalUtility
import com.andromeda.ara.skills.ListSkills
import com.andromeda.ara.util.SetFeedData
import com.andromeda.ara.util.TagManager
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.iconRes
import com.mikepenz.materialdrawer.model.interfaces.nameText
import com.mikepenz.materialdrawer.model.interfaces.selectedColorRes
import com.mikepenz.materialdrawer.util.setItems
import com.mikepenz.materialdrawer.widget.MaterialDrawerSliderView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class Drawer {
    @Throws(IOException::class)
    fun main(drawerItem: Long, ctx: Context, setFeedData: SetFeedData): ArrayList<FeedModel>? {
        var lat = 0.0
        var log = 0.0
        val feedModel1 = arrayListOf<FeedModel>()
        val locationManager = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            println("running location system")

            val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                log = location.longitude
                lat = location.latitude
            }

        }



        when (drawerItem) {
            DrawerModeConstants.HOME -> {
                feedModel1.addAll(News().newsGeneral(ctx, setFeedData))
                return feedModel1
            }
            DrawerModeConstants.DEVICES ->{
                GlobalScope.launch {
                    try {
//                        setFeedData.setData(Actions().getAll())
                    }
                    catch (e:Exception){
                        e.printStackTrace()
                    }
                }
                return null

            }
            DrawerModeConstants.FOOD -> {
                GlobalScope.launch {
//                    setFeedData.setData(Food().getFood(log.toString(), lat.toString()))
                }
                return null
            }
            DrawerModeConstants.SHORTCUTS -> {
                GlobalScope.launch {
//                    setFeedData.setData(ListSkills().main())
                }
                return null
            }
            DrawerModeConstants.CAL -> {
                feedModel1.addAll( CalUtility.readCalendarEvent(ctx))
                return feedModel1
            }
            DrawerModeConstants.REMINDERS ->{
                GlobalScope.launch {
//                    setFeedData.setData(Reminders().get())
                }
                return null
            }
            104L -> {
//               feedModel1.addAll(News().newsGeneral(setFeedData))
                return feedModel1
            }
            102L -> {
//                feedModel1.addAll(News().newsTech(setFeedData))
                return feedModel1
            }
            105L -> {
//                feedModel1.addAll(News().newsMoney(setFeedData))
                return feedModel1
            }
            else -> {
                println("returning")
                return null

            }
        }

    }
    companion object{
        fun newItem(id:Long, text:String, res:Int): SecondaryDrawerItem {
            val drawerItem = SecondaryDrawerItem()
            drawerItem.nameText = text
            drawerItem.identifier = id
            drawerItem.iconRes = res
            drawerItem.selectedColorRes = R.color.md_white_1000
            return drawerItem
        }
        fun drawer(act:Activity, materialDrawerSliderView: MaterialDrawerSliderView, setFeedData: SetFeedData){
            val home = newItem(DrawerModeConstants.HOME, "Home", R.drawable.home)
            val food = newItem(DrawerModeConstants.FOOD, "Food", R.drawable.food)
            val cal = newItem(DrawerModeConstants.CAL, "Agenda", R.drawable.ic_today_black_24dp)
            val shortcuts = newItem(DrawerModeConstants.SHORTCUTS, "Shortcuts", R.drawable.shortcut)
            val devices = newItem(DrawerModeConstants.DEVICES, "Devices", R.drawable.devices)
            val reminders = newItem(DrawerModeConstants.REMINDERS, "Reminders", R.drawable.done)
            val tech = newItem(102, "Tech", R.drawable.technews)
            val domestic = newItem(104, "Domestic", R.drawable.domnews)
            val money = newItem(105, "Money", R.drawable.money)
            val news = newItem(101, "News", R.drawable.news);
            news.setSubItems(tech, domestic, money)
            materialDrawerSliderView.onDrawerItemClickListener = { view: View?, iDrawerItem: IDrawerItem<*>, i: Int ->
                act.runOnUiThread{
                val tabs = act.findViewById<RecyclerView>(R.id.tabs);
                tabs.setVisibility(View.INVISIBLE);
                try {
                    Drawer().main(iDrawerItem.identifier,act, setFeedData);
                } catch (e:Exception) {
                    e.printStackTrace();
                }

            }
                false
            }
            materialDrawerSliderView.setItems(home, food, cal, shortcuts, devices, reminders, news)
            materialDrawerSliderView.setBackgroundResource(R.color.colorBack)
            val accountHeaderView = AccountHeaderView(act)
            val profile = ProfileDrawerItem()
            profile.nameText = User.name
            accountHeaderView.addProfile(profile, 0)
            materialDrawerSliderView.accountHeader = accountHeaderView


        }
    }
}
