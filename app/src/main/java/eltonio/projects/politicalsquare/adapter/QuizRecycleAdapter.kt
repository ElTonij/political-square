package eltonio.projects.politicalsquare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eltonio.projects.politicalsquare.R
import eltonio.projects.politicalsquare.databinding.LayoutResultItemBinding
import eltonio.projects.politicalsquare.util.Ideologies
import eltonio.projects.politicalsquare.util.Ideologies.Companion.resString
import eltonio.projects.politicalsquare.model.QuizResult
import eltonio.projects.politicalsquare.views.ResultListPointView
import kotlinx.android.synthetic.main.layout_result_item.view.*

class QuizRecycleAdapter(val context: Context) : RecyclerView.Adapter<QuizRecycleAdapter.QuizRecycleViewHolder>() {
    var onQuizItemClickListener: ((position: Int) -> Unit)? = null

    private val DIFFER_CALLBACK = object: DiffUtil.ItemCallback<QuizResult>() {
        override fun areItemsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean = oldItem == newItem
        override fun areContentsTheSame(oldItem: QuizResult, newItem: QuizResult): Boolean = oldItem == newItem
    }
    private val differ: AsyncListDiffer<QuizResult> = AsyncListDiffer(this, DIFFER_CALLBACK)

    fun addQuizResultList(resultList: List<QuizResult>) {
        differ.submitList(resultList)
        //notifyDataSetChanged()
    }

    inner class QuizRecycleViewHolder(val binding: LayoutResultItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizRecycleViewHolder {
        val view = LayoutResultItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuizRecycleViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizRecycleViewHolder, position: Int) {
        val item = differ.currentList[position]

        holder.binding.apply {
            textSavedResultDate.text = item.endedAt

            layoutItemContainer.transitionName = "transition_item_containter_$position"

            for (ideology in Ideologies.values()) {
                if (ideology.stringId == item.ideologyStringId) {
                    textSavedResultTitle.text = ideology.titleRes.resString(context)
                }
            }

            textSavedResultNumber.text = (position+1).toString()

            horStartScore = item.horStartScore
            verStarScore = item.verStartScore
            horResultScore = item.horResultScore
            verResultScore = item.verResultScore

            val myView = ResultListPointView(context, horResultScore, verResultScore)

            frameQuizResultImage.addView(myView)

            root.setOnClickListener {
                onQuizItemClickListener?.invoke(position)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    fun getQuizResultAt(position: Int) = differ.currentList[position]

    companion object {
        var horStartScore = 0
        var verStarScore = 0
        var horResultScore = 0
        var verResultScore = 0
    }
}
