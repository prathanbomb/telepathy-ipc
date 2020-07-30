package th.co.digio.poolshark.telepathy_ipc

import android.content.*
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf

/**
 * Created by supitsara on 10/6/2020 AD.
 */
class Synapse {

    fun startConnection(listener: ConnectionListener, targetService: String) {
        connectionListener = listener
        mTargetService = targetService
        transmit(bundleOf(Pair(HANDSHAKE, Event.HANDSHAKE_REQUEST.name)))
    }

    fun registerListener(listener: DataListener) {
        if (connectionListener != null) {
            clientListener = listener
        } else {
            serviceListener = listener
        }
    }

    fun transmit(data: Bundle) {
        val customIntent = Intent()
        customIntent.action = Constants.ACTION
        customIntent.putExtra(Constants.SENDER, mContext?.packageName)
        customIntent.putExtra(Constants.TARGET, mTargetService)
        customIntent.putExtra(Constants.DATA, data)
        mContext?.sendBroadcast(customIntent)
    }

    companion object {
        private const val HANDSHAKE = "handshake"
        private var mContext: Context? = null
        private var synapse: Synapse? = null
        private var clientListener: DataListener? = null
        private var serviceListener: DataListener? = null
        private var connectionListener: ConnectionListener? = null
        private var mTargetService: String = ""

        private fun init(context: Context?) {
            mContext = context
        }

        @JvmStatic
        fun instance(): Synapse {
            if (synapse == null) {
                synapse = Synapse()
            }
            return synapse!!
        }
    }

    class Dendrite : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val sender = intent.getStringExtra(Constants.SENDER)
            val targetService = intent.getStringExtra(Constants.TARGET)
            val recvData = intent.getBundleExtra(Constants.DATA)
            if (sender != mContext?.packageName) {
                if (!targetService.isNullOrEmpty()) {
                    if (recvData?.getString(HANDSHAKE) == Event.HANDSHAKE_REQUEST.name) {
                        Log.d("connection", "$sender has registered")
                        instance().transmit(
                            bundleOf(
                                Pair(
                                    HANDSHAKE,
                                    Event.HANDSHAKE_RESPONSE.name
                                )
                            )
                        )
                    } else {
                        serviceListener?.onReceived(recvData)
                    }
                } else {
                    if (recvData?.getString(HANDSHAKE) == Event.HANDSHAKE_RESPONSE.name) {
                        connectionListener?.onConnected()
                    } else {
                        clientListener?.onReceived(recvData)
                    }
                }
            }
        }
    }

    class SynapseProvider : ContentProvider() {
        override fun insert(uri: Uri, values: ContentValues?): Uri? {
            return null
        }

        override fun query(
            uri: Uri,
            projection: Array<out String>?,
            selection: String?,
            selectionArgs: Array<out String>?,
            sortOrder: String?
        ): Cursor? {
            return null
        }

        override fun onCreate(): Boolean {
            init(context)
            return true
        }

        override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<out String>?
        ): Int {
            return 0
        }

        override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
            return 0
        }

        override fun getType(uri: Uri): String? {
            return null
        }
    }

    private enum class Event {
        HANDSHAKE_REQUEST,
        HANDSHAKE_RESPONSE
    }

    interface DataListener {
        fun onReceived(data: Bundle?)
    }

    interface ConnectionListener {
        fun onConnected()
        fun onDisconnected()
    }
}