package com.example.wym_002.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import com.example.wym_002.R
import com.example.wym_002.databinding.FragmentAnaliticsFragmentBinding
import org.eazegraph.lib.models.PieModel


class AnaliticsFragment : Fragment() {

    private lateinit var binding: FragmentAnaliticsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnaliticsFragmentBinding.inflate(layoutInflater)


        buttonToEnterDate()
        buttonToSetList()
        buttonBack()
        buttonForward()

        setData()


        return binding.root
    }

    private fun buttonForward() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewForward.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthForward()

        }

    }

    private fun setCurrentMonthForward() {
        //TODO(НАПИСАТЬ ЗАМЕНУ МЕСЯЦА НА СЛЕДУЮЩИЙ)
    }

    private fun buttonBack() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewBack.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthBack()

        }

    }

    private fun setCurrentMonthBack() {
        //TODO(НАПИСАТЬ ЗАМЕНУ МЕСЯЦА НА ПРЕДЫДУЩИЙ)
    }


    private fun buttonToEnterDate(){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutDate.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogEnterDate()

        }

    }

    private fun showDialogEnterDate() {
        //TODO(ВЫВОД ДИАЛОГА С ВЫБОРОМ ПРОМЕЖУТКА)
    }


    private fun buttonToSetList() {

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutSetList.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            //TODO(ДОПИСАТЬ ПЕРЕХОД НА ДРУГОЙ ФРАГМЕНТ)

        }

    }


    @SuppressLint("ResourceType")
    private fun setData() {

        //TODO(СДЕЛАТЬ ПОДСЧЕТ И ВЫВОД В ДИАГРАММУ)

        binding.piechart.addPieSlice(
            PieModel(
                "house", 3F,
                Color.parseColor(getString(R.color.house))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "bus", 4F,
                Color.parseColor(getString(R.color.bus))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "foodHouse", 5.5F,
                Color.parseColor(getString(R.color.food_house))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "health", 8F,
                Color.parseColor(getString(R.color.health))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "coffee", 20F,
                Color.parseColor(getString(R.color.coffee))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "games", 8F,
                Color.parseColor(getString(R.color.games))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "clothes", 20F,
                Color.parseColor(getString(R.color.clothes))
            )
        )
        binding.piechart.addPieSlice(
            PieModel(
                "another", 31.5F,
                Color.parseColor(getString(R.color.another))
            )
        )

        binding.piechart.animationTime = 600

        binding.piechart.startAnimation()
    }

}