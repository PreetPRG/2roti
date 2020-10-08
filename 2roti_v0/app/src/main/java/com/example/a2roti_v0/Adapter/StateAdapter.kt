package com.example.a2roti_v0.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.a2roti_v0.Interfaces.OnItemClickStateListener
import com.example.a2roti_v0.Model.State
import com.example.a2roti_v0.R


class StateAdapter // endregion
//region Constructor
(private val context: Context, private val states: List<State>,
        // region Variables
 private val listener: OnItemClickStateListener
) : RecyclerView.Adapter<StateAdapter.ViewHolder>() {

    // endregion
    // region Adapter Methods
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_state, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val state = states[position]
        holder.stateName.text = state.stateName
        state.loadFlagByCode(context)
        if (state.flag != -1) {
            holder.stateFlagImageView.setImageResource(state.flag)
        }
        holder.rootView.setOnClickListener { listener.onItemClickStateListener(state) }
    }

    override fun getItemCount(): Int {
        return states.size
    }

    // endregion
    // region ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val stateFlagImageView: ImageView
        val stateName: TextView
        val rootView: LinearLayout

        init {
            stateFlagImageView = itemView.findViewById(R.id.state_flag)
            stateName = itemView.findViewById(R.id.state_title)
            rootView = itemView.findViewById(R.id.rootStateView)
        }
    } // endregion

}