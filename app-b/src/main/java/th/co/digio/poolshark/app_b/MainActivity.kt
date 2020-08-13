package th.co.digio.poolshark.app_b

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.activity_main.*
import th.co.digio.poolshark.telepathy_ipc.Synapse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Synapse.instance().startConnection(object : Synapse.ConnectionListener {
            override fun onConnected() {
                tv.text = "Connected to App A"
                Synapse.instance().transmit(bundleOf(Pair("request", "Message from App B")))
            }

            override fun onDisconnected() {
                Log.d("onDisconnected", "disconnected")
            }
        }, "th.co.digio.poolshark.app_a")
        Synapse.instance().registerListener(object : Synapse.DataListener {
            override fun onReceived(data: Bundle?) {
                Toast.makeText(this@MainActivity, data?.getString("return"), LENGTH_SHORT).show()
            }
        })
    }
}
