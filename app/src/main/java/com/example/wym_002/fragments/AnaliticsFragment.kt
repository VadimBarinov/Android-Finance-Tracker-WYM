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
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import app.com.kotlinapp.OnSwipeTouchListener
import com.example.wym_002.ItemAdapter
import com.example.wym_002.ItemDataClass
import com.example.wym_002.R
import com.example.wym_002.databinding.*
import com.example.wym_002.db.MainDb
import com.example.wym_002.hidingPanel
import org.eazegraph.lib.models.PieModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates


class AnaliticsFragment : Fragment(), ItemAdapter.Listener {

    private lateinit var binding: FragmentAnaliticsFragmentBinding

    private lateinit var dialog: Dialog
    private lateinit var dialogSetDate: DialogSetDateBinding
    private lateinit var dialogSetDateDatepicker: DialogSetDateDatepickerBinding

    private lateinit var dialogSetDateOnListView: DialogSetDateListBinding
    private lateinit var dialogSetCardOnListView: DialogSetCardBinding
    private lateinit var dialogItemInfo: DialogItemInfoBinding

    lateinit var db: MainDb

    lateinit var pref: SharedPreferences

    private var getCardFromCalcList = "all"
    private var getDateFromDiagram = "month"
    private var getDateFromList = "all"

    private val adapter = ItemAdapter(this)

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

        buttonToSetList()       // меняет экраны

        getDateFromDiagram = "month"
        getDateFromList = "all"

        buttonToEnterDate()       // выбор даты для диаграммы

        val calendar = Calendar.getInstance()
        calcSetMonthDiagram(calendar)    // показывает диаграмму за месяц по умолчанию



        buttonToEnterDateOnListView()      // выбор даты для листа

        getCardFromCalcList = "all"

        buttonToSetCardOnListView("all", "all")      // выбор карты для листа (за все время)

        calcSetAllCardsList()             // выбирает все финансы по умолчанию

        calcSetAllList()              // показывает лист за все время по умолчанию



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

        binding.constraintLayotDiagram.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {
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

        binding.constraintLayotDiagram.setOnTouchListener(object : OnSwipeTouchListener(this.activity!!) {})

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

    @SuppressLint("ResourceType")
    private fun showDialogEnterDate() {

        dialogSetDate = DialogSetDateBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDate.root)
        dialog.setCancelable(true)

        dialogSetDate.textViewWeek.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDate.textViewMonth.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDate.textViewYear.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDate.textViewAnother.setTextColor(Color.parseColor(getString(R.color.greyWYM)))

        when (getDateFromDiagram) {
            "week" -> dialogSetDate.textViewWeek.setTextColor(Color.parseColor(getString(R.color.backColor)))
            "month" -> dialogSetDate.textViewMonth.setTextColor(Color.parseColor(getString(R.color.backColor)))
            "year" -> dialogSetDate.textViewYear.setTextColor(Color.parseColor(getString(R.color.backColor)))
            else -> dialogSetDate.textViewAnother.setTextColor(Color.parseColor(getString(R.color.backColor)))

        }

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

            getDateFromDiagram = "week"

            calcSetWeekDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewMonth.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromDiagram = "month"

            calcSetMonthDiagram(Calendar.getInstance())

            dialog.dismiss()

        }

        dialogSetDate.textViewYear.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromDiagram = "year"

            calcSetYearDiagram()

            dialog.dismiss()

        }

        dialogSetDate.textViewAnother.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromDiagram = "another"

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

            lateinit var totalSpends: String

            val threadTotalSpends = Thread {
                totalSpends = db.getDao().getSumByDate(
                    dateFormatDiagram.format(dateFromTemp.time),
                    dateFormatDiagram.format(dateToTemp.time)).toString()
            }
            threadTotalSpends.start()
            threadTotalSpends.join()

            binding.textViewTotalSpend2.text = totalSpends

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

        lateinit var totalSpends: String

        val threadTotalSpends = Thread {
            totalSpends = db.getDao().getSumByDate(dateFrom, dateTo).toString()
        }
        threadTotalSpends.start()
        threadTotalSpends.join()

        binding.textViewTotalSpend2.text = totalSpends

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

        lateinit var totalSpends: String

        val threadTotalSpends = Thread {
            totalSpends = db.getDao().getSumByDate(dateFrom, dateTo).toString()
        }
        threadTotalSpends.start()
        threadTotalSpends.join()

        binding.textViewTotalSpend.text = totalSpends

        lateinit var totalSaving: String

        val threadSaving = Thread {
            totalSaving = db.getDao().getSavingByDate(dateSavingFrom, dateSavingTo).toString()
        }
        threadSaving.start()
        threadSaving.join()

        binding.textViewSaveSpend.text = totalSaving

        binding.piechart.clearChart()       // чтобы не накладывал на предыдущее

        val totalSpend = Integer.parseInt(binding.textViewTotalSpend.text.toString())   // 100 %

        if (totalSpend != 0){

            binding.textViewMonth.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))
            binding.textViewYear.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))
            binding.textViewYear2.setTextColor(Color.parseColor(getString(R.color.secondaryWhite)))

            var calcValueFloat by Delegates.notNull<Float>()

            var houseValue by Delegates.notNull<Int>()

            val threadHouse = Thread {
                houseValue = db.getDao().getSumWithCategoryByDate(getString(R.string.house), dateFrom, dateTo)
                calcValueFloat = (houseValue * 100F) / totalSpend
            }
            threadHouse.start()
            threadHouse.join()
            binding.textViewHouseSum.text = houseValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "house", calcValueFloat,
                    Color.parseColor(getString(R.color.house))
                )
            )

            var busValue by Delegates.notNull<Int>()

            val threadBus = Thread {
                busValue = db.getDao().getSumWithCategoryByDate(getString(R.string.bus), dateFrom, dateTo)
                calcValueFloat = (busValue * 100F) / totalSpend
            }
            threadBus.start()
            threadBus.join()
            binding.textViewBusSum.text = busValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "bus", calcValueFloat,
                    Color.parseColor(getString(R.color.bus))
                )
            )

            var foodHouseValue by Delegates.notNull<Int>()

            val threadFoodHouse = Thread {
                foodHouseValue = db.getDao().getSumWithCategoryByDate(getString(R.string.food_house), dateFrom, dateTo)
                calcValueFloat = (foodHouseValue * 100F) / totalSpend
            }
            threadFoodHouse.start()
            threadFoodHouse.join()
            binding.textViewFoodHouseSum.text = foodHouseValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "foodHouse", calcValueFloat,
                    Color.parseColor(getString(R.color.food_house))
                )
            )

            var healthValue by Delegates.notNull<Int>()

            val threadHealth = Thread {
                healthValue = db.getDao().getSumWithCategoryByDate(getString(R.string.health), dateFrom, dateTo)
                calcValueFloat = (healthValue * 100F) / totalSpend
            }
            threadHealth.start()
            threadHealth.join()
            binding.textViewHealthSum.text = healthValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "health", calcValueFloat,
                    Color.parseColor(getString(R.color.health))
                )
            )

            var coffeeValue by Delegates.notNull<Int>()

            val threadCoffee = Thread {
                coffeeValue = db.getDao().getSumWithCategoryByDate(getString(R.string.coffee), dateFrom, dateTo)
                calcValueFloat = (coffeeValue * 100F) / totalSpend
            }
            threadCoffee.start()
            threadCoffee.join()
            binding.textViewCoffeeSum.text = coffeeValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "coffee", calcValueFloat,
                    Color.parseColor(getString(R.color.coffee))
                )
            )

            var gamesValue by Delegates.notNull<Int>()

            val threadGames = Thread {
                gamesValue = db.getDao().getSumWithCategoryByDate(getString(R.string.games), dateFrom, dateTo)
                calcValueFloat = (gamesValue * 100F) / totalSpend
            }
            threadGames.start()
            threadGames.join()
            binding.textViewGamesSum.text = gamesValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "games", calcValueFloat,
                    Color.parseColor(getString(R.color.games))
                )
            )

            var clothesValue by Delegates.notNull<Int>()

            val threadClothes = Thread {
                clothesValue = db.getDao().getSumWithCategoryByDate(getString(R.string.clothes), dateFrom, dateTo)
                calcValueFloat = (clothesValue * 100F) / totalSpend
            }
            threadClothes.start()
            threadClothes.join()
            binding.textViewClothesSum.text = clothesValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "clothes", calcValueFloat,
                    Color.parseColor(getString(R.color.clothes))
                )
            )

            var anotherValue by Delegates.notNull<Int>()

            val threadAnother = Thread {
                anotherValue = db.getDao().getSumWithCategoryByDate(getString(R.string.another), dateFrom, dateTo)
                calcValueFloat = (anotherValue * 100F) / totalSpend
            }
            threadAnother.start()
            threadAnother.join()
            binding.textViewAnotherSum.text = anotherValue.toString()
            binding.piechart.addPieSlice(
                PieModel(
                    "another", calcValueFloat,
                    Color.parseColor(getString(R.color.another))
                )
            )

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

            binding.viewflipper.showNext()

        }

        binding.constraintLayoutSetDiagram.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            binding.viewflipper.showPrevious()

        }

    }



    private fun buttonToSetCardOnListView(dateFrom: String, dateTo: String){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutSetCard.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogSetCardOnListView(dateFrom, dateTo)

        }

    }

    private fun showDialogSetCardOnListView(dateFrom: String, dateTo: String) {

        dialogSetCardOnListView = DialogSetCardBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetCardOnListView.root)
        dialog.setCancelable(true)

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogSetCardOnListView.textViewCard.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetCardList()

            if (dateFrom != "all" && dateTo != "all"){
                calcListViewWithDate(dateFrom, dateTo)
            }
            else{
                calcListViewAllItems()
            }

            dialog.dismiss()

        }

        dialogSetCardOnListView.textViewWallet.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetWalletList()

            if (dateFrom != "all" && dateTo != "all"){
                calcListViewWithDate(dateFrom, dateTo)
            }
            else{
                calcListViewAllItems()
            }

            dialog.dismiss()

        }

        dialogSetCardOnListView.textViewBank.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetBankList()

            if (dateFrom != "all" && dateTo != "all"){
                calcListViewWithDate(dateFrom, dateTo)
            }
            else{
                calcListViewAllItems()
            }

            dialog.dismiss()

        }

        dialogSetCardOnListView.textViewAll.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            calcSetAllCardsList()

            if (dateFrom != "all" && dateTo != "all"){
                calcListViewWithDate(dateFrom, dateTo)
            }
            else{
                calcListViewAllItems()
            }

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    private fun calcSetAllCardsList() {

        binding.imageViewCard.visibility = View.GONE
        binding.textViewCard.visibility = View.VISIBLE

        getCardFromCalcList = "all"

    }

    private fun calcSetBankList() {

        binding.imageViewCard.visibility = View.VISIBLE
        binding.textViewCard.visibility = View.GONE

        binding.imageViewCard2.setImageResource(R.drawable.account_balance)

        getCardFromCalcList = "account"

    }

    private fun calcSetWalletList() {

        binding.imageViewCard.visibility = View.VISIBLE
        binding.textViewCard.visibility = View.GONE

        binding.imageViewCard2.setImageResource(R.drawable.wallet)

        getCardFromCalcList = "wallet"

    }

    private fun calcSetCardList() {

        binding.imageViewCard.visibility = View.VISIBLE
        binding.textViewCard.visibility = View.GONE

        binding.imageViewCard2.setImageResource(R.drawable.credit_card)

        getCardFromCalcList = "card"

    }

    private fun buttonToEnterDateOnListView(){

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        binding.constraintLayoutDateList.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            showDialogEnterDateOnListView()

        }

    }

    @SuppressLint("ResourceType")
    private fun showDialogEnterDateOnListView() {

        dialogSetDateOnListView = DialogSetDateListBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogSetDateOnListView.root)
        dialog.setCancelable(true)

        dialogSetDateOnListView.textViewWeek.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDateOnListView.textViewMonth.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDateOnListView.textViewYear.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDateOnListView.textViewAll.setTextColor(Color.parseColor(getString(R.color.greyWYM)))
        dialogSetDateOnListView.textViewAnother.setTextColor(Color.parseColor(getString(R.color.greyWYM)))

        when (getDateFromList) {
            "week" -> dialogSetDateOnListView.textViewWeek.setTextColor(Color.parseColor(getString(R.color.backColor)))
            "month" -> dialogSetDateOnListView.textViewMonth.setTextColor(Color.parseColor(getString(R.color.backColor)))
            "year" -> dialogSetDateOnListView.textViewYear.setTextColor(Color.parseColor(getString(R.color.backColor)))
            "all" -> dialogSetDateOnListView.textViewAll.setTextColor(Color.parseColor(getString(R.color.backColor)))
            else -> dialogSetDateOnListView.textViewAnother.setTextColor(Color.parseColor(getString(R.color.backColor)))

        }

        // параметры анимации нажатия
        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogSetDateOnListView.textViewWeek.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromList = "week"

            calcSetWeekList()

            dialog.dismiss()

        }

        dialogSetDateOnListView.textViewMonth.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromList = "month"

            calcSetMonthList()

            dialog.dismiss()

        }

        dialogSetDateOnListView.textViewYear.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromList = "year"

            calcSetYearList()

            dialog.dismiss()

        }

        dialogSetDateOnListView.textViewAll.setOnClickListener {

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromList = "all"

            calcSetAllList()

            dialog.dismiss()

        }

        dialogSetDateOnListView.textViewAnother.setOnClickListener{

            it.startAnimation(buttonClick1)
            it.startAnimation(buttonClick2)
            it.visibility = View.VISIBLE

            getDateFromList = "another"

            dialog.dismiss()

            calcSetAnotherList()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    @SuppressLint("SetTextI18n")
    private fun calcSetAnotherList() {

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

            val dateFormatDiagram = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

            calcListViewWithDate(dateFormatDiagram.format(dateFromTemp.time),
                        dateFormatDiagram.format(dateToTemp.time))

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()
    }

    private fun calcSetAllList() {
        calcListViewAllItems()
    }

    private fun calcSetYearList() {

        val dateFrom = "${Calendar.getInstance().get(Calendar.YEAR)}-01-01 00:00:00"
        val dateTo = "${Calendar.getInstance().get(Calendar.YEAR)}-12-31 23:59:59"

        calcListViewWithDate(dateFrom, dateTo)

    }

    private fun calcSetMonthList() {

        val calendar = Calendar.getInstance()

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

        calcListViewWithDate(dateFrom, dateTo)

    }

    @SuppressLint("SimpleDateFormat")
    private fun calcSetWeekList() {

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

        calcListViewWithDate(dateFrom, dateTo)

    }

    private fun calcListViewWithDate(dateFrom: String, dateTo: String){

        buttonToSetCardOnListView(dateFrom, dateTo)

        binding.rcView.layoutManager = LinearLayoutManager(this.activity!!)
        binding.rcView.adapter = adapter

        adapter.clearItemList()

        when (getCardFromCalcList != "all"){
            true -> {
                db.getDao().getItemsByDateWithSelectedCard(dateFrom, dateTo, getCardFromCalcList)
                    .asLiveData().observe(this.activity!!){

                        it.forEach{ it1 ->

                            val iconCat = when (it1.category){

                                getString(R.string.house) -> { R.drawable.house_diagram }
                                getString(R.string.bus) -> { R.drawable.bus_diagram }
                                getString(R.string.food_house) -> { R.drawable.food_house_diagram }
                                getString(R.string.health) -> { R.drawable.health_diagram }
                                getString(R.string.coffee) -> { R.drawable.coffee_diagram }
                                getString(R.string.games) -> { R.drawable.games_diagram }
                                getString(R.string.clothes) -> { R.drawable.clothes_diagram }
                                else -> { R.drawable.another_diagram }

                            }

                            val cardIcon = when (it1.card){

                                "card" -> { R.drawable.credit_card }
                                "wallet" -> { R.drawable.wallet }
                                else -> { R.drawable.account_balance }

                            }

                            // 16.12.2023 13:35
                            val dateList = "${it1.data[8]}${it1.data[9]}." +
                                    "${it1.data[5]}${it1.data[6]}." +
                                    "${it1.data[0]}${it1.data[1]}${it1.data[2]}${it1.data[3]}      " +
                                    "${it1.data[11]}${it1.data[12]}${it1.data[13]}${it1.data[14]}${it1.data[15]}"

                            val item = ItemDataClass(iconCat,
                                it1.category,
                                it1.spend,
                                dateList,
                                it1.amount,
                                cardIcon)

                            adapter.addItemInList(item)
                        }

                    }
            }
            else ->{
                db.getDao().getItemsByDateWithAllCards(dateFrom, dateTo)
                    .asLiveData().observe(this.activity!!){

                        it.forEach{ it1 ->

                            val iconCat = when (it1.category){

                                getString(R.string.house) -> { R.drawable.house_diagram }
                                getString(R.string.bus) -> { R.drawable.bus_diagram }
                                getString(R.string.food_house) -> { R.drawable.food_house_diagram }
                                getString(R.string.health) -> { R.drawable.health_diagram }
                                getString(R.string.coffee) -> { R.drawable.coffee_diagram }
                                getString(R.string.games) -> { R.drawable.games_diagram }
                                getString(R.string.clothes) -> { R.drawable.clothes_diagram }
                                else -> { R.drawable.another_diagram }

                            }

                            val cardIcon = when (it1.card){

                                "card" -> { R.drawable.credit_card }
                                "wallet" -> { R.drawable.wallet }
                                else -> { R.drawable.account_balance }

                            }

                            // 16.12.2023 13:35
                            val dateList = "${it1.data[8]}${it1.data[9]}." +
                                    "${it1.data[5]}${it1.data[6]}." +
                                    "${it1.data[0]}${it1.data[1]}${it1.data[2]}${it1.data[3]}      " +
                                    "${it1.data[11]}${it1.data[12]}${it1.data[13]}${it1.data[14]}${it1.data[15]}"

                            val item = ItemDataClass(iconCat,
                                it1.category,
                                it1.spend,
                                dateList,
                                it1.amount,
                                cardIcon)

                            adapter.addItemInList(item)
                        }

                    }
            }
        }

        lateinit var spendRes: String

        val threadTotalSpendsList = Thread {

        // вывод суммы трат с выбранной картой и датой либо по всем картам и с датой
            spendRes = when (getCardFromCalcList){
                "card" -> {
                    db.getDao().getSumByDateWithCards(dateFrom, dateTo, "card").toString()
                }
                "wallet" -> {
                    db.getDao().getSumByDateWithCards(dateFrom, dateTo, "wallet").toString()
                }
                "account" -> {
                    db.getDao().getSumByDateWithCards(dateFrom, dateTo, "account").toString()
                }
                else ->{
                    db.getDao().getSumByDate(dateFrom, dateTo).toString()
                }
            }

        }
        threadTotalSpendsList.start()
        threadTotalSpendsList.join()

        binding.textViewTotalSpendList.text = spendRes

    }

    private fun calcListViewAllItems(){

        buttonToSetCardOnListView("all", "all")


        binding.rcView.layoutManager = LinearLayoutManager(this.activity!!)
        binding.rcView.adapter = adapter

        adapter.clearItemList()

        when (getCardFromCalcList != "all"){
            true -> {
                db.getDao().getItemsByAllDateWithSelectedCard(getCardFromCalcList)
                    .asLiveData().observe(this.activity!!){

                        it.forEach{ it1 ->

                            val iconCat = when (it1.category){

                                getString(R.string.house) -> { R.drawable.house_diagram }
                                getString(R.string.bus) -> { R.drawable.bus_diagram }
                                getString(R.string.food_house) -> { R.drawable.food_house_diagram }
                                getString(R.string.health) -> { R.drawable.health_diagram }
                                getString(R.string.coffee) -> { R.drawable.coffee_diagram }
                                getString(R.string.games) -> { R.drawable.games_diagram }
                                getString(R.string.clothes) -> { R.drawable.clothes_diagram }
                                else -> { R.drawable.another_diagram }

                            }

                            val cardIcon = when (it1.card){

                                "card" -> { R.drawable.credit_card }
                                "wallet" -> { R.drawable.wallet }
                                else -> { R.drawable.account_balance }

                            }

                            // 16.12.2023 13:35
                            val dateList = "${it1.data[8]}${it1.data[9]}." +
                                    "${it1.data[5]}${it1.data[6]}." +
                                    "${it1.data[0]}${it1.data[1]}${it1.data[2]}${it1.data[3]}      " +
                                    "${it1.data[11]}${it1.data[12]}${it1.data[13]}${it1.data[14]}${it1.data[15]}"

                            val item = ItemDataClass(iconCat,
                                it1.category,
                                it1.spend,
                                dateList,
                                it1.amount,
                                cardIcon)

                            adapter.addItemInList(item)
                        }

                    }
            }
            else ->{
                db.getDao().getAllItemsWithAllCards()
                    .asLiveData().observe(this.activity!!){

                        it.forEach{ it1 ->

                            val iconCat = when (it1.category){

                                getString(R.string.house) -> { R.drawable.house_diagram }
                                getString(R.string.bus) -> { R.drawable.bus_diagram }
                                getString(R.string.food_house) -> { R.drawable.food_house_diagram }
                                getString(R.string.health) -> { R.drawable.health_diagram }
                                getString(R.string.coffee) -> { R.drawable.coffee_diagram }
                                getString(R.string.games) -> { R.drawable.games_diagram }
                                getString(R.string.clothes) -> { R.drawable.clothes_diagram }
                                else -> { R.drawable.another_diagram }

                            }

                            val cardIcon = when (it1.card){

                                "card" -> { R.drawable.credit_card }
                                "wallet" -> { R.drawable.wallet }
                                else -> { R.drawable.account_balance }

                            }

                            // 16.12.2023 13:35
                            val dateList = "${it1.data[8]}${it1.data[9]}." +
                                    "${it1.data[5]}${it1.data[6]}." +
                                    "${it1.data[0]}${it1.data[1]}${it1.data[2]}${it1.data[3]}      " +
                                    "${it1.data[11]}${it1.data[12]}${it1.data[13]}${it1.data[14]}${it1.data[15]}"

                            val item = ItemDataClass(iconCat,
                                it1.category,
                                it1.spend,
                                dateList,
                                it1.amount,
                                cardIcon)

                            adapter.addItemInList(item)
                        }

                    }
            }
        }


        lateinit var spendRes: String

        val threadTotalSpendsList = Thread {

        // вывод суммы трат с выбранной картой и по всем картам за все время
            spendRes = when (getCardFromCalcList){
                "card" -> {
                    db.getDao().getSumByAllDateWithCards("card").toString()
                }
                "wallet" -> {
                    db.getDao().getSumByAllDateWithCards("wallet").toString()

                }
                "account" -> {
                    db.getDao().getSumByAllDateWithCards("account").toString()
                }
                else ->{
                    db.getDao().getSumByAllDateWithAllCards().toString()
                }
            }

        }
        threadTotalSpendsList.start()
        threadTotalSpendsList.join()

        binding.textViewTotalSpendList.text = spendRes

    }

    private fun showDialogItemInfo(item: ItemDataClass) {

        val buttonClick1 = AlphaAnimation(1f, 0.7f)
        buttonClick1.duration = 160
        buttonClick1.fillAfter = false
        val buttonClick2 = AlphaAnimation(0.7f, 1f)
        buttonClick2.duration = 50
        buttonClick2.fillAfter = true
        buttonClick2.startOffset = 70

        dialogItemInfo = DialogItemInfoBinding.inflate(layoutInflater)
        dialog = Dialog(this.activity!!)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(dialogItemInfo.root)
        dialog.setCancelable(true)

        dialogItemInfo.imageViewIconCat.setImageResource(item.iconCat)
        dialogItemInfo.textViewCategory.text = item.categoryString
        dialogItemInfo.textViewSpend.text = item.spend.toString()
        dialogItemInfo.imageViewCard.setImageResource(item.cardIcon)
        dialogItemInfo.textViewDate2.text = item.date
        dialogItemInfo.textViewCatInfo.text = item.string

        dialogItemInfo.dialogBtn.setOnClickListener {

            dialog.dismiss()

        }

        hidingPanel(dialog)
        dialog.show()

    }

    override fun onClick(item: ItemDataClass) {
        showDialogItemInfo(item)
    }

}