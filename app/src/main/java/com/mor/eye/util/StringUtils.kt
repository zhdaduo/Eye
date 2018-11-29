package com.mor.eye.util

import android.content.Context
import com.mor.eye.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.collections.HashMap
import kotlin.collections.Map
import kotlin.collections.dropLastWhile
import kotlin.collections.set
import kotlin.collections.toTypedArray

object StringUtils {
    fun durationFormat(duration: Int): String {
        return durationFormat(duration.toLong()).substring(0, 6).replace("' ", ":")
    }

    fun durationFormat(duration: Long): String {
        val minute = duration / 60.toLong()
        val second = duration % 60.toLong()
        return if (minute <= 9.toLong())
            (if (second <= 9.toLong())
                "" + '0'.toString() + minute + "' 0" + second +
                        "''"
            else
                "" + '0'.toString() + minute + "' " + second + "''")
        else
            (if (second <= 9.toLong())
                ("" +
                        minute + "' 0" + second + "''")
            else
                "$minute' $second''")
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = if (isToday(date)) {
            SimpleDateFormat("HH:mm")
        } else SimpleDateFormat("yyyy/MM/dd")

        return format.format(date)
    }

    fun isToday(inputJudgeDate: Date): Boolean {
        var flag = false
        val longDate = System.currentTimeMillis()
        val nowDate = Date(longDate)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val format = dateFormat.format(nowDate)
        val subDate = format.substring(0, 10)
        val beginTime = "$subDate 00:00:00"
        val endTime = "$subDate 23:59:59"
        var parseBeginTime: Date? = null
        var parseEndTime: Date? = null
        try {
            parseBeginTime = dateFormat.parse(beginTime)
            parseEndTime = dateFormat.parse(endTime)
        } catch (e: ParseException) {
        }
        if (inputJudgeDate.after(parseBeginTime) && inputJudgeDate.before(parseEndTime)) {
            flag = true; }
        return flag
    }

    fun isOnTimeStamp(startDate: Long, endDate: Long): Boolean {
        val longDate = System.currentTimeMillis()
        return longDate in (startDate + 1)..(endDate - 1)
    }

    fun getCreateTime(context: Context, olderTime: Long): String {
        val currentTime = System.currentTimeMillis()
        val hour = (currentTime - olderTime) / 1000 / 3600
        return when {
            hour < 24 -> String.format(context.getString(R.string.create_date_hour), hour.toString())
            hour < 24 * 30 -> String.format(context.getString(R.string.create_date_day), (hour / 24).toString())
            else -> String.format(context.getString(R.string.create_date_month), (hour / (24 * 30)).toString())
        }
    }

    fun formatUrl(url: String): String {
        return when {
            url.contains("date=") -> url.split("date=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            url.contains("start=") -> url.split("start=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
            url.contains("title=") -> url.split("title=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            else -> url.split("page=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }
    }

    /**
     * 解析出url参数中的键值对
     * 如 "index.jsp?Action=del&id=123"，解析出Action:del,id:123存入map中
     * @param URL url地址
     * @return url请求参数部分
     */
    fun urlRequest(URL: String): Map<String, String> {
        val mapRequest = HashMap<String, String>()
        val arrSplit: Array<String>
        val strUrlParam = truncateUrlPage(URL) ?: return mapRequest
        // 每个键值为一组 www.2cto.com
        arrSplit = strUrlParam.split("[&]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (strSplit in arrSplit) {
            val arrSplitEqual: Array<String> = strSplit.split("[=]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            // 解析出键值
            if (arrSplitEqual.size > 1) {
                // 正确解析
                mapRequest[arrSplitEqual[0]] = arrSplitEqual[1]
            } else {
                if (arrSplitEqual[0] != "") {
                    // 只有参数没有值，不加入
                    mapRequest[arrSplitEqual[0]] = ""
                }
            }
        }
        return mapRequest
    }

    /**
     * 去掉url中的路径，留下请求参数部分
     * @param strURL url地址
     * @return url请求参数部分
     */
    private fun truncateUrlPage(strURL: String): String? {
        var strURL = strURL
        var strAllParam: String? = null
        val arrSplit: Array<String>
        strURL = strURL.trim { it <= ' ' }
        arrSplit = strURL.split("[?]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (strURL.length > 1) {
            if (arrSplit.size > 1) {
                if (!arrSplit[1].isEmpty()) {
                    strAllParam = arrSplit[1]
                }
            }
        }
        return strAllParam
    }
}
