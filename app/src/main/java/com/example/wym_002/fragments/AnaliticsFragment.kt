package com.example.wym_002.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.AlphaAnimation
import androidx.fragment.app.Fragment
import app.com.kotlinapp.OnSwipeTouchListener
import com.example.wym_002.R
import com.example.wym_002.databinding.DialogSetDateBinding
import com.example.wym_002.databinding.DialogSetDateDatepickerBinding
import com.example.wym_002.databinding.FragmentAnaliticsFragmentBinding
import com.example.wym_002.db.MainDb
import com.example.wym_002.hidingPanel
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*


class AnaliticsFragment : Fragment() {

    private lateinit var binding: FragmentAnaliticsFragmentBinding

    private lateinit var dialog: Dialog
    private lateinit var dialogSetDate: DialogSetDateBinding
    private lateinit var dialogSetDateDatepicker: DialogSetDateDatepickerBinding

    lateinit var db: MainDb

    lateinit var pref: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAnaliticsFragmentBinding.inflate(layoutInflater)

        db = MainDb.getDb(this.activity!!)

        pref = context!!.getSharedPreferences("pref", Context.MODE_PRIVATE)
        // resultCardBalance        key: R.drawable.credit_card_white.toString()
        // resultWalletBalance      key: R.drawable.wallet_white.toString()
        // resultBankBalance        key: R.drawable.account_balance_white.toString()
        //
        // progressBarMainProgress          key: mainProgress
        // progressBarMainMax               key: mainMax
        // progressBarMainColor               key: mainColor
        // progressBarSecondProgress        key: secondProgress
        // progressBarSecondMax             key: secondMax
        // progressBarSecondColor             key: secondColor
        // progressBarSavingProgress        key: savingProgress
        // progressBarSavingMax             key: savingMax
        // progressBarSavingColor             key: savingColor
        //
        // checkSplashScreen        key: getString(R.string.flagSplashScreen)      ОБРАТНЫЕ ПЕРЕМЕННЫЕ
        //                                                                        0 == TRUE   1 == FALSE
        // setDateDay               key: getString(R.string.setDateDay)
        //
        // deleteDateLast                   key: getString(R.string.deleteDateLast)

        buttonToEnterDate()
        buttonToSetList()

        val calendar = Calendar.getInstance()
        calcSetMonthDiagram(calendar)

        return binding.root
    }

    private fun setCurrentMonthBack(calendar: Calendar) {    // замена месяца на предыдущий

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH) - 2))

        calcSetMonthDiagram(calendar)

    }

    private fun setCurrentMonthForward(calendar: Calendar) {  // замена месяца на следующий

        calendar.set(Calendar.MONTH, (calendar.get(Calendar.MONTH)))

        calcSetMonthDiagram(calendar)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun buttonsAndSwipeOnDisplayEnabled(calendar: Calendar){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.imageViewBack.visibility = View.VISIBLE
        binding.imageViewForward.visibility = View.VISIBLE

        binding.imageViewBack2.visibility = View.VISIBLE
        binding.imageViewForward2.visibility = View.VISIBLE

        binding.imageViewForward.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthForward(calendar)

        }

        binding.imageViewBack.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            setCurrentMonthBack(calendar)

        }

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                setCurrentMonthForward(calendar)
            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                setCurrentMonthBack(calendar)
            }
        })

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun buttonsAndSwipeOnDisplayDisabled(){

        binding.imageViewBack.visibility = View.GONE
        binding.imageViewForward.visibility = View.GONE

        binding.imageViewBack2.visibility = View.GONE
        binding.imageViewForward2.visibility = View.GONE

        binding.imageViewForward.setOnClickListener{}

        binding.imageViewBack.setOnClickListener{}

        binding.root.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {})

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

        dialogSetDate = DialogSetDateBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDate.root)
        dialog.setCancelable(true)

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogSetDate.textViewWeek.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetWeekDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewMonth.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetMonthDiagram(Calendar.getInstance())

            dialog.dismiss()

        }

        dialogSetDate.textViewYear.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetYearDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewAnother.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            dialog.dismiss()

            calcSetAnotherDiagram()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun calcSetAnotherDiagram() {

        dialogSetDateDatepicker = DialogSetDateDatepickerBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDateDatepicker.root)
        dialog.setCancelable(true)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        var dateFromTemp = Calendar.getInstance()
        dateFromTemp.set(Calendar.HOUR_OF_DAY, 0)
        dateFromTemp.set(Calendar.MINUTE, 0)
        dateFromTemp.set(Calendar.SECOND, 0)

        var dateToTemp = Calendar.getInstance()
        dateToTemp.set(Calendar.HOUR_OF_DAY, 23)
        dateToTemp.set(Calendar.MINUTE, 59)
        dateToTemp.set(Calendar.SECOND, 59)

        val calendar = Calendar.getInstance()
        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        when (dateFromTemp.get(Calendar.DAY_OF_MONTH) != 1){
            true -> {
                dateFromTemp.set(Calendar.DAY_OF_MONTH, dateToTemp.get(Calendar.DAY_OF_MONTH) - 1)
            }
            else -> {
                var month = dateFromTemp.get(Calendar.MONTH) - 1
                var year = dateFromTemp.get(Calendar.YEAR)
                if (month == -1) {
                    month = 11
                    year -= 1
                }
                dateFromTemp.set(Calendar.MONTH, month)
                dateFromTemp.set(Calendar.YEAR, year)
                dateFromTemp.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
            }
        }

        dialogSetDateDatepicker.enterDataFrom.text = "От: ${dateFormat.format(dateFromTemp.time)}"
        dialogSetDateDatepicker.enterDataTo.text = "До: ${dateFormat.format(dateToTemp.time)}"

        dialogSetDateDatepicker.enterDataFrom.setOnClickListener{

            dateFromTemp = showDataTimePickerWithMax(dateToTemp.timeInMillis, dateFromTemp)

        }

        dialogSetDateDatepicker.enterDataTo.setOnClickListener{

            dateToTemp = showDataTimePickerWithMin(dateFromTemp.timeInMillis, dateToTemp)

        }

        dialogSetDateDatepicker.dialogBtn.setOnClickListener{

            binding.textViewSave.visibility = View.GONE
            binding.textViewSaveSpend.visibility = View.GONE

            binding.textViewTotalSpend.visibility = View.GONE
            binding.textViewTotalSpend2.visibility = View.VISIBLE

            binding.textViewMonth.visibility = View.VISIBLE
            binding.textViewYear.visibility = View.VISIBLE
            binding.textViewYear2.visibility = View.GONE

            buttonsAndSwipeOnDisplayDisabled()

            binding.textViewMonth.text = "От: ${dateFormat.format(dateFromTemp.time)}"
            binding.textViewYear.text = "До: ${dateFormat.format(dateToTemp.time)}"

            val dateFormatDiagram = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            val threadTotalSpends = Thread {
                binding.textViewTotalSpend2.text = db.getDao().getSumByDate(
                    dateFormatDiagram.format(dateFromTemp.time),
                    dateFormatDiagram.format(dateToTemp.time)).toString()
            }
            threadTotalSpends.start()
            threadTotalSpends.join()

            calcDiagram(dateFormatDiagram.format(dateFromTemp.time),
                        dateFormatDiagram.format(dateToTemp.time),
                        dateFormatDiagram.format(dateFromTemp.time),
                        dateFormatDiagram.format(dateFromTemp.time))

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun calcSetYearDiagram() {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        binding.textViewTotalSpend.visibility = View.VISIBLE
        binding.textViewTotalSpend2.visibility = View.GONE

        binding.textViewMonth.visibility = View.GONE
        binding.textViewYear.visibility = View.GONE
        binding.textViewYear2.visibility = View.VISIBLE    // год по центру

        binding.textViewYear2.text = Calendar.getInstance().get(Calendar.YEAR).toString()

        buttonsAndSwipeOnDisplayDisabled()

        val dateFrom = "${Calendar.getInstance().get(Calendar.YEAR)}-01-01 00:00:00"
        val dateTo = "${Calendar.getInstance().get(Calendar.YEAR)}-12-31 23:59:59"

        calcDiagram(dateFrom, dateTo, dateFrom, dateTo)

    }

    private fun calcSetMonthDiagram(calendar: Calendar) {

        binding.textViewSave.visibility = View.VISIBLE
        binding.textViewSaveSpend.visibility = View.VISIBLE

        binding.textViewTotalSpend.visibility = View.VISIBLE
        binding.textViewTotalSpend2.visibility = View.GONE

        binding.textViewMonth.visibility = View.VISIBLE
        binding.textViewYear.visibility = View.VISIBLE
        binding.textViewYear2.visibility = View.GONE

        buttonsAndSwipeOnDisplayEnabled(calendar)

        binding.textViewMonth.text = when (calendar.get(Calendar.MONTH) + 1){
            1 -> "Январь"
            2 -> "Февраль"
            3 -> "Март"
            4 -> "Апрель"
            5 -> "Май"
            6 -> "Июнь"
            7 -> "Июль"
            8 -> "Август"
            9 -> "Сентябрь"
            10 -> "Октябрь"
            11 -> "Ноябрь"
            else -> "Декабрь"
        }

        binding.textViewYear.text = calendar.get(Calendar.YEAR).toString()

        val month = calendar.get(Calendar.MONTH)
        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val resetDay = when (arrayOfMonths[month] <
                (pref.getInt(getString(R.string.setDateDay), 0) + 1)){
            true -> arrayOfMonths[month]
            else -> (pref.getInt(getString(R.string.setDateDay), 0) + 1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.DAY_OF_MONTH, resetDay)

        val dateFrom = dateFormat.format(calendar.time)          // дата "от"
        val dateSaving = calcDate(calendar)                 // дата для вывода сбережений


        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        when (month < 11){
            true -> {
                if (resetDay - 1 == 0){
                    calendar.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
                }
                else if (resetDay - 1 >= arrayOfMonths[month + 1]){
                    calendar.set(Calendar.DAY_OF_MONTH, (arrayOfMonths[month + 1] - 1))
                    calendar.set(Calendar.MONTH, (month + 1))
                }
                else {
                    calendar.set(Calendar.DAY_OF_MONTH, (resetDay - 1))
                    calendar.set(Calendar.MONTH, (month + 1))
                }
            }
            else -> {
                if (resetDay - 1 == 0){
                    calendar.set(Calendar.DAY_OF_MONTH, arrayOfMonths[month])
                }
                else if (resetDay - 1 >= arrayOfMonths[0]){
                    calendar.set(Calendar.DAY_OF_MONTH, (arrayOfMonths[0] - 1))
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + 1))
                }
                else {
                    calendar.set(Calendar.DAY_OF_MONTH, (resetDay - 1))
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + 1))
                }
            }
        }
        val dateTo = dateFormat.format(calendar.time)           // дата "до"

        if (resetDay == 1) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.DAY_OF_MONTH, resetDay)
            when (month < 11) {
                true -> calendar.set(Calendar.MONTH, (month + 1))
                else -> {
                    calendar.set(Calendar.MONTH, 0)
                    calendar.set(Calendar.YEAR, (calendar.get(Calendar.YEAR) + 1))
                }
            }
        }

        calcDiagram(dateFrom, dateTo, dateSaving, dateSaving)

    }

    @SuppressLint("SimpleDateFormat")
    private fun calcSetWeekDiagram() {

        binding.textViewSave.visibility = View.GONE
        binding.textViewSaveSpend.visibility = View.GONE

        binding.textViewTotalSpend.visibility = View.GONE
        binding.textViewTotalSpend2.visibility = View.VISIBLE

        binding.textViewMonth.visibility = View.GONE
        binding.textViewYear.visibility = View.GONE
        binding.textViewYear2.visibility = View.VISIBLE

        binding.textViewYear2.text = "Текущая неделя"

        buttonsAndSwipeOnDisplayDisabled()

        val calendarDate = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEEE")
        var dayOfTheWeek = dateFormat.format(calendarDate.time)   // день недели строкой

        while (dayOfTheWeek != "понедельник"){      // в итоге получим дату ближайшего понедельника
            calendarDate.set(Calendar.DAY_OF_MONTH, (calendarDate.get(Calendar.DAY_OF_MONTH) - 1))
            dayOfTheWeek = dateFormat.format(calendarDate.time)
        }

        val dateFormatDiagram = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        calendarDate.set(Calendar.HOUR_OF_DAY, 0)
        calendarDate.set(Calendar.MINUTE, 0)
        calendarDate.set(Calendar.SECOND, 0)
        val dateFrom = dateFormatDiagram.format(calendarDate.time)

        calendarDate.set(Calendar.DAY_OF_MONTH, (calendarDate.get(Calendar.DAY_OF_MONTH) + 6))
        calendarDate.set(Calendar.HOUR_OF_DAY, 23)
        calendarDate.set(Calendar.MINUTE, 59)
        calendarDate.set(Calendar.SECOND, 59)
        val dateTo = dateFormatDiagram.format(calendarDate.time)

        val threadTotalSpends = Thread {
            binding.textViewTotalSpend2.text = db.getDao().getSumByDate(dateFrom, dateTo).toString()
        }
        threadTotalSpends.start()
        threadTotalSpends.join()

        calcDiagram(dateFrom, dateTo, dateFrom, dateTo)

    }

    @SuppressLint("SetTextI18n")
    private fun showDataTimePickerWithMax(maxDate: Long, selectedDate: Calendar): Calendar{

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                selectedDate.set(Calendar.HOUR_OF_DAY, 0)
                selectedDate.set(Calendar.MINUTE, 0)
                selectedDate.set(Calendar.SECOND, 0)

                dialogSetDateDatepicker.enterDataFrom.text = "От: ${dateFormat.format(selectedDate.time)}"

                binding.textViewMonth.text = "От: ${dateFormat.format(selectedDate.time)}"

            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        hidingPanel(datePickerDialog)
        datePickerDialog.datePicker.maxDate = maxDate
        datePickerDialog.show()

        return selectedDate

    }

    @SuppressLint("SetTextI18n")
    private fun showDataTimePickerWithMin(minDate: Long, selectedDate: Calendar): Calendar{

        val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        val datePickerDialog = DatePickerDialog(
            this.activity!!, { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                selectedDate.set(year, monthOfYear, dayOfMonth)
                selectedDate.set(Calendar.HOUR_OF_DAY, 23)
                selectedDate.set(Calendar.MINUTE, 59)
                selectedDate.set(Calendar.SECOND, 59)

                dialogSetDateDatepicker.enterDataTo.text = "До: ${dateFormat.format(selectedDate.time)}"

                binding.textViewYear.text = "До: ${dateFormat.format(selectedDate.time)}"

            },
            selectedDate.get(Calendar.YEAR),
            selectedDate.get(Calendar.MONTH),
            selectedDate.get(Calendar.DAY_OF_MONTH)
        )

        hidingPanel(datePickerDialog)
        datePickerDialog.datePicker.minDate = minDate
        datePickerDialog.datePicker.maxDate = Calendar.getInstance().timeInMillis
        datePickerDialog.show()

        return selectedDate

    }


    @SuppressLint("ResourceType")
    private fun calcDiagram(dateFrom: String, dateTo: String, dateSavingFrom: String, dateSavingTo: String) {

        val threadTotalSpends = Thread {
            binding.textViewTotalSpend.text = db.getDao().getSumByDate(dateFrom, dateTo).toString()
        }
        threadTotalSpends.start()
        threadTotalSpends.join()

        val threadSaving = Thread {
            binding.textViewSaveSpend.text = db.getDao().getSavingByDate(dateSavingFrom, dateSavingTo).toString()
        }
        threadSaving.start()
        threadSaving.join()

        binding.piechart.clearChart()       // чтобы не накладывал на предыдущее

        val totalSpend = Integer.parseInt(binding.textViewTotalSpend.text.toString())   // 100 %

        if (totalSpend != 0){

            binding.textViewMonth.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))
            binding.textViewYear.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))
            binding.textViewYear2.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))

            val threadHouse = Thread {

                val houseValue = db.getDao().getSumWithCategoryByDate(getString(R.string.house), dateFrom, dateTo)
                val calcValueFloat = (houseValue * 100F) / totalSpend

                binding.textViewHouseSum.text = houseValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "house", calcValueFloat,
                        Color.parseColor(getString(R.color.house))
                    )
                )
            }
            threadHouse.start()
            threadHouse.join()

            val threadBus = Thread {

                val busValue = db.getDao().getSumWithCategoryByDate(getString(R.string.bus), dateFrom, dateTo)
                val calcValueFloat = (busValue * 100F) / totalSpend

                binding.textViewBusSum.text = busValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "bus", calcValueFloat,
                        Color.parseColor(getString(R.color.bus))
                    )
                )
            }
            threadBus.start()
            threadBus.join()

            val threadFoodHouse = Thread {

                val foodHouseValue = db.getDao().getSumWithCategoryByDate(getString(R.string.food_house), dateFrom, dateTo)
                val calcValueFloat = (foodHouseValue * 100F) / totalSpend

                binding.textViewFoodHouseSum.text = foodHouseValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "foodHouse", calcValueFloat,
                        Color.parseColor(getString(R.color.food_house))
                    )
                )
            }
            threadFoodHouse.start()
            threadFoodHouse.join()

            val threadHealth = Thread {

                val healthValue = db.getDao().getSumWithCategoryByDate(getString(R.string.health), dateFrom, dateTo)
                val calcValueFloat = (healthValue * 100F) / totalSpend

                binding.textViewHealthSum.text = healthValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "health", calcValueFloat,
                        Color.parseColor(getString(R.color.health))
                    )
                )
            }
            threadHealth.start()
            threadHealth.join()

            val threadCoffee = Thread {

                val coffeeValue = db.getDao().getSumWithCategoryByDate(getString(R.string.coffee), dateFrom, dateTo)
                val calcValueFloat = (coffeeValue * 100F) / totalSpend

                binding.textViewCoffeeSum.text = coffeeValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "coffee", calcValueFloat,
                        Color.parseColor(getString(R.color.coffee))
                    )
                )
            }
            threadCoffee.start()
            threadCoffee.join()

            val threadGames = Thread {

                val gamesValue = db.getDao().getSumWithCategoryByDate(getString(R.string.games), dateFrom, dateTo)
                val calcValueFloat = (gamesValue * 100F) / totalSpend

                binding.textViewGamesSum.text = gamesValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "games", calcValueFloat,
                        Color.parseColor(getString(R.color.games))
                    )
                )
            }
            threadGames.start()
            threadGames.join()

            val threadClothes = Thread {

                val clothesValue = db.getDao().getSumWithCategoryByDate(getString(R.string.clothes), dateFrom, dateTo)
                val calcValueFloat = (clothesValue * 100F) / totalSpend

                binding.textViewClothesSum.text = clothesValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "clothes", calcValueFloat,
                        Color.parseColor(getString(R.color.clothes))
                    )
                )
            }
            threadClothes.start()
            threadClothes.join()

            val threadAnother = Thread {

                val anotherValue = db.getDao().getSumWithCategoryByDate(getString(R.string.another), dateFrom, dateTo)
                val calcValueFloat = (anotherValue * 100F) / totalSpend

                binding.textViewAnotherSum.text = anotherValue.toString()

                binding.piechart.addPieSlice(
                    PieModel(
                        "another", calcValueFloat,
                        Color.parseColor(getString(R.color.another))
                    )
                )
            }
            threadAnother.start()
            threadAnother.join()

        }
        else{

            binding.textViewMonth.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
            binding.textViewYear.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
            binding.textViewYear2.setTextColor(Color.parseColor(getString(R.color.greyWYM)))

            binding.textViewHouseSum.text = "0"
            binding.textViewBusSum.text = "0"
            binding.textViewFoodHouseSum.text = "0"
            binding.textViewHealthSum.text = "0"
            binding.textViewCoffeeSum.text = "0"
            binding.textViewGamesSum.text = "0"
            binding.textViewClothesSum.text = "0"
            binding.textViewAnotherSum.text = "0"

            binding.piechart.addPieSlice(
                PieModel(
                    "empty", 100F,
                    Color.parseColor(getString(R.color.greyWYM))
                )
            )

        }

        binding.piechart.animationTime = 600

        binding.piechart.startAnimation()
    }

    private fun calcDate(calendar: Calendar): String{

        // ЕСЛИ ДАТА БОЛЬШЕ ИЛИ РАВНА ДАТЕ НАЧАЛА УЧЕТА, ТО БЮДЖЕТ ДОБАВЛЯЕТСЯ В ПЕРВЫЙ ДЕНЬ ТЕКУЩЕГО МЕСЯЦА
        // ЕСЛИ ЖЕ ДАТА МЕНЬШЕ, ТО БЮДЖЕТ ДОБАВЛЯЕСЯ В ПЕРВОЕ ЧИСЛО ПРОШЛОГО МЕСЯЦА

        val arrayOfMonths = when (calendar.get(Calendar.YEAR) % 4 == 0) {
            true -> {
                arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
            else ->{
                arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            }
        }

        val setDateDayTemp = when (arrayOfMonths[calendar.get(Calendar.MONTH)] <
                (pref.getInt(getString(R.string.setDateDay), 0) + 1)){
            true -> arrayOfMonths[calendar.get(Calendar.MONTH)]
            else -> (pref.getInt(getString(R.string.setDateDay), 0) + 1)
        }

        when (setDateDayTemp <= calendar.get(Calendar.DAY_OF_MONTH)){
            true -> {
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                return dateFormat.format(calendar.time)
            }
            else -> {
                var month = calendar.get(Calendar.MONTH) - 1
                var year = calendar.get(Calendar.YEAR)
                if (month == -1) {
                    month = 11
                    year -= 1
                }
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.DAY_OF_MONTH, 1)
                calendar.set(Calendar.HOUR_OF_DAY, 0)
                calendar.set(Calendar.MINUTE, 0)
                calendar.set(Calendar.SECOND, 0)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                return dateFormat.format(calendar.time)
            }
        }
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


}