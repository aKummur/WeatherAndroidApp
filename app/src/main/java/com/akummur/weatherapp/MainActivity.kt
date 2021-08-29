package com.akummur.weatherapp

import android.app.Dialog
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.URL

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CallAPILoginAsynctask("ak","2345").execute()
    }

    private inner class CallAPILoginAsynctask(val user: String, val password: String) :
        AsyncTask<Any, Void, String>() {
        private  lateinit var customProgressDialog: Dialog

        override fun onPreExecute() {
            super.onPreExecute()
            showProgressDailog()
        }

        override fun doInBackground(vararg params: Any?): String {
            var result: String
            var connection: HttpURLConnection? = null

            try{
                var url = URL("https://run.mocky.io/v3/9bb81bc6-5078-4068-bc36-dccc694d025a")
                connection = url.openConnection() as HttpURLConnection
                connection.doInput = true // get data
                connection.doOutput = true // send data

                connection.instanceFollowRedirects = false
                connection.requestMethod = "POST"
                connection.setRequestProperty("content-type", "application/json")
                connection.setRequestProperty("charset", "utf-8")
                connection.setRequestProperty("Accept", "application/json")

                connection.useCaches = false

                val writeDataOutputStream = DataOutputStream(connection.outputStream)
                val jsonReq = JSONObject()
                jsonReq.put("username", user)
                jsonReq.put("password", password)

                writeDataOutputStream.writeBytes(jsonReq.toString())
                writeDataOutputStream.flush()
                writeDataOutputStream.close()

                val httpResult: Int = connection.responseCode
                if(httpResult == HttpURLConnection.HTTP_OK)
                {
                    val inputStream = connection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    try{
                        while (reader.readLine().also { line = it } != null)
                        {
                            stringBuilder.append(line + "\n")
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } finally {
                        try{
                            inputStream.close()
                        } catch (e: IOException)
                        {
                            e.printStackTrace()
                        }
                    }
                    result = stringBuilder.toString()
                } else {
                    result = connection.responseMessage
                }
            } catch (e: SocketTimeoutException) {
                result = "Connection Timeout"
            } catch (e: Exception) {
                result = "Error : " + e.message
            } finally {
                connection?.disconnect()
            }
            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            cancelProgressDailog()
            Log.i("JSON response", result!!)

            val jsonObject = JSONObject(result)
            val a = jsonObject.optString("a")
        }

        private fun showProgressDailog() {
            customProgressDialog = Dialog(this@MainActivity)
            customProgressDialog.setContentView(R.layout.dailog_custom_progress)
            customProgressDialog.show()
        }

        private fun cancelProgressDailog() {
            customProgressDialog.dismiss()
        }
    }
}