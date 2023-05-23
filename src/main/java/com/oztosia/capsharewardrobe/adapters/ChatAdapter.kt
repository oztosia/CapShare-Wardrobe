package com.oztosia.capsharewardrobe.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.oztosia.capsharewardrobe.CapShareWardrobe
import com.oztosia.capsharewardrobe.R
import com.oztosia.capsharewardrobe.adapters.viewholders.GenericViewHolder
import com.oztosia.capsharewardrobe.constants.FirebaseConst
import com.oztosia.capsharewardrobe.constants.MessagesTypesConst.Companion.RECEIVE_MESSAGE_TYPE
import com.oztosia.capsharewardrobe.constants.MessagesTypesConst.Companion.SEND_MESSAGE_TYPE
import com.oztosia.capsharewardrobe.models.Message
import com.oztosia.capsharewardrobe.utils.DateFormatter
import java.lang.IllegalArgumentException

class ChatAdapter()
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val application: Context = CapShareWardrobe.getAppContext()
    val dataSet: ArrayList<Message> = arrayListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            RECEIVE_MESSAGE_TYPE -> {
                ReceiveMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_receive_message, parent, false)
                )
            }
            SEND_MESSAGE_TYPE -> {
                SendMessageViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_send_message, parent, false)
                )
            }
            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReceiveMessageViewHolder -> holder.bind(dataSet[position])
            is SendMessageViewHolder -> holder.bind(dataSet[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (dataSet.get(position).authorUid == FirebaseConst.CURRENT_USER) {
            return SEND_MESSAGE_TYPE
        } else if (dataSet.get(position).authorUid != FirebaseConst.CURRENT_USER) {
            return RECEIVE_MESSAGE_TYPE
        } else {
            throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    inner class SendMessageViewHolder(view: View) : GenericViewHolder<Message>(view) {

        var sendMessageTextView: TextView = view.findViewById(R.id.send_message_text)
        var sendMessageImageView: ImageView = view.findViewById(R.id.sendMessageImageView)
        var date: TextView = view.findViewById(R.id.date)

        override fun bind(item: Message) {
            val timestamp = DateFormatter.convertFromMillisToDate(item.date.toLong())
            date.text = timestamp
            sendMessageTextView.text = item.text
            if (item.downloadUrl.isNotEmpty()) {
                sendMessageImageView.visibility = View.VISIBLE
                Glide.with(application).load(item.downloadUrl).into(sendMessageImageView)
            }
            else{sendMessageImageView.visibility = View.GONE}
            sendMessageImageView.setOnClickListener {
                val bundle = bundleOf("uri" to item.downloadUrl)
                Navigation.findNavController(it).navigate(R.id.SingleImageFragment, bundle)
            }
            itemView.setOnClickListener {
                if (date.visibility == View.GONE)
                    date.visibility = View.VISIBLE
                else
                    date.visibility = View.GONE
            }
        }
    }

        inner class ReceiveMessageViewHolder(view: View) : GenericViewHolder<Message>(view) {

            var receiveMessageTextView: TextView = view.findViewById(R.id.receive_message_text)
            var receiveMessageImageView: ImageView = view.findViewById(R.id.receiveMessageImageView)
            var date: TextView = view.findViewById(R.id.date)

            override fun bind(item: Message) {
                val timestamp = DateFormatter.convertFromMillisToDate(item.date.toLong())
                date.text = timestamp
                receiveMessageTextView.text = item.text
                if (item.downloadUrl.isNotEmpty()) {
                    receiveMessageImageView.visibility = View.VISIBLE
                    Glide.with(application).load(item.downloadUrl).into(receiveMessageImageView)
                }
                else{receiveMessageImageView.visibility = View.GONE}
                receiveMessageImageView.setOnClickListener {
                    val bundle = bundleOf("uri" to item.downloadUrl)
                    Navigation.findNavController(it).navigate(R.id.SingleImageFragment, bundle)
                }
                itemView.setOnClickListener {
                    if (date.visibility == View.GONE)
                        date.visibility = View.VISIBLE
                    else
                        date.visibility = View.GONE
                }
            }

        }

}