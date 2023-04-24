package com.example.mypersonalwardrobe.adapters.viewholders

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.mypersonalwardrobe.MyPersonalWardrobe
import com.example.mypersonalwardrobe.R
import com.example.mypersonalwardrobe.constants.FirebaseConst
import com.example.mypersonalwardrobe.models.Message


class MessageViewHolder (view: View)
    : GenericViewHolder<Message>(view) {

    val application: Context = MyPersonalWardrobe.getAppContext()

    var sendMessageTextView: TextView = view.findViewById(R.id.send_message_text)
    var receiveMessageTextView: TextView = view.findViewById(R.id.receive_message_text)
    var receiveMessageImageView: ImageView = itemView.findViewById(R.id.receiveMessageImageView)
    var sendMessageImageView: ImageView = itemView.findViewById(R.id.sendMessageImageView)

    override fun bind(item: Message) {

        if (item.authorUid.equals(FirebaseConst.CURRENT_USER)) {

            sendMessageTextView.visibility = View.VISIBLE
            receiveMessageTextView.visibility = View.GONE
            sendMessageTextView.text = item.text

        }
        else if (!item.authorUid.equals(FirebaseConst.CURRENT_USER)) {
                sendMessageTextView.visibility = View.GONE
                receiveMessageTextView.visibility = View.VISIBLE
                receiveMessageTextView.text = item.text
            }


        if (item.downloadUrl.isNotEmpty()) {
            if (item.authorUid.equals(FirebaseConst.CURRENT_USER)) {
                sendMessageImageView.visibility = View.VISIBLE
                Glide.with(application).load(item.downloadUrl)
                    .centerCrop()
                    .into(sendMessageImageView)
            }
            else if (!item.authorUid.equals(FirebaseConst.CURRENT_USER)) {
                receiveMessageImageView.visibility = View.VISIBLE
                Glide.with(application).load(item.downloadUrl)
                    .centerCrop()
                    .into(receiveMessageImageView)

            }
        }
        else if (item.downloadUrl.isEmpty()) {
            sendMessageImageView.visibility = View.GONE
            receiveMessageImageView.visibility = View.GONE
        }


        sendMessageTextView.setOnClickListener {
            Toast.makeText(
                application, "data: " + item.toString(), Toast.LENGTH_SHORT).show()
        }

        receiveMessageTextView.setOnClickListener {
            Toast.makeText(
                application, "data: " + item.toString(), Toast.LENGTH_SHORT).show()
        }

        receiveMessageImageView.setOnClickListener {

                val bundle = bundleOf("uri" to item.downloadUrl)
                Navigation.findNavController(it).navigate(R.id.SingleImageFragment, bundle)

        }

        sendMessageImageView.setOnClickListener {
            val bundle = bundleOf("uri" to item.downloadUrl)
            Navigation.findNavController(it).navigate(R.id.SingleImageFragment, bundle)
        }

    }
}
