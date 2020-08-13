package th.co.digio.poolshark.app_a

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import kotlinx.android.synthetic.main.activity_main.*
import th.co.digio.poolshark.telepathy_ipc.Synapse

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Synapse.instance().registerListener(object : Synapse.DataListener {
            override fun onReceived(data: Bundle?) {
                tv.text = data?.getString("request")
                Synapse.instance().transmit(bundleOf(Pair("return", "Message from App A")))
            }
        })
    }
}
