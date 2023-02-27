package com.cesar.mnexpedicoes.utils

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.toSpanned
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cesar.mnexpedicoes.R
import com.cesar.mnexpedicoes.features.home.model.Location
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import jp.wasabeef.glide.transformations.BlurTransformation
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

private const val SIZE_OF_FULL_HEX_COLOR = 7

val Int.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

// String
fun String.removeHtmlTagsFromString(): String {
    return this.replace(Regex("<.*?>"), "")
}

fun String.parseHtml(): Spanned? {
    return when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ->
            HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).trim().toSpanned()
        else -> Html.fromHtml(this)?.trim()?.toSpanned()
    }
}

fun CharSequence.unaccent(): String {
    var string = this
    val out = CharArray(string.length)
    string = Normalizer.normalize(string, Normalizer.Form.NFD)
    var j = 0
    for (i in string.indices) {
        val c = string[i]
        if (c <= '\u007F') out[j++] = c
    }
    return String(out)
}

fun <T> T?.stringify(toString: (T) -> String = { it.toString() }): String =
    if (this == null) "" else toString(this)

// Int
fun Int.hexString(): String {
    return "#${Integer.toHexString(this).substring(2)}"
}

fun Int.stringHHMMFromTimeInterval(): String {

    val minutes = (this / 60) % 60
    val hours = (this / 3600)

    return String.format("%02d:%02d", hours, minutes)
}

fun Int.stringHHMMSSFromTimeInterval(): String {

    val seconds = this % 60
    val minutes = (this / 60) % 60
    val hours = (this / 3600)

    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun Int.stringMMSS(): String {
    val seconds = this % 60
    val minutes = (this / 60) % 60

    return String.format("%02d:%02d", minutes, seconds)
}

// View
fun View.toggleVisibility() {
    visibility = if (visibility == View.VISIBLE) {
        View.INVISIBLE
    } else {
        View.VISIBLE
    }
}

fun View.toggleVisibility(visible: Boolean = false) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.toggleVisibility(visible: Boolean = false, isGone: Boolean = true) {
    visibility =
        if (visible) View.VISIBLE
        else if (!visible && !isGone) View.INVISIBLE
        else View.GONE
}

fun View.disable() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        background.colorFilter =
            BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                Color.GRAY,
                BlendModeCompat.MULTIPLY
            )
    } else {
        background.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY)
    }
    isEnabled = false
}

fun View.enable() {
    background.colorFilter = null
    isEnabled = true
}

fun View.isVisible(): Boolean {
    if (!isShown) {
        return false
    }
    val actualPosition = Rect()
    val isGlobalVisible = getGlobalVisibleRect(actualPosition)
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    val screen = Rect(0, 0, screenWidth, screenHeight)
    return isGlobalVisible && Rect.intersects(actualPosition, screen)
}

fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

fun TextView.setTextColorRes(@ColorRes color: Int) = setTextColor(context.getColorCompat(color))

fun TextView.makeUnderlined() {
    paintFlags = paintFlags or Paint.UNDERLINE_TEXT_FLAG
}

fun TextView.makeBold() {
    setTypeface(typeface, Typeface.BOLD)
}

fun TextView.makeRegular() {
    setTypeface(typeface, Typeface.NORMAL)
}

fun TextView.makeSemiBold() {
    val semiBoldTypeface = ResourcesCompat.getFont(context, R.font.montserrat_semibold)
    typeface = semiBoldTypeface
}

fun TextView.makeNormal() {
    setTypeface(Typeface.create(typeface, Typeface.NORMAL), Typeface.NORMAL)
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeInVisible() {
    visibility = View.INVISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun String.capitalizeFirstChar(): String {
    val stringsList = arrayListOf<String>()
    this.lowercase().split(" ").forEach { text ->
        val capitalizedText = text.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
        }
        stringsList.add(capitalizedText)
    }
    return stringsList.joinToString(" ")
}

// GradientDrawable
fun GradientDrawable.setCornerRadius(
    topLeft: Float = 0F,
    topRight: Float = 0F,
    bottomRight: Float = 0F,
    bottomLeft: Float = 0F
) {
    cornerRadii = arrayOf(
        topLeft,
        topLeft,
        topRight,
        topRight,
        bottomRight,
        bottomRight,
        bottomLeft,
        bottomLeft
    ).toFloatArray()
}

fun TextView.setTextGradient(colors: IntArray) {
    val width = this.paint.measureText(this.text.toString())
    val textShader: Shader = LinearGradient(
        0f,
        0f,
        width,
        this.textSize,
        colors.map { this.resources.getColor(it, null) }.toIntArray(),
        null,
        Shader.TileMode.CLAMP
    )
    this.paint.shader = textShader
}

// SnackBar
enum class SnackBarType {
    Success, Error, Warning
}

fun Fragment.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Fragment.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Activity.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun Activity.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun View.showSnackWith(message: String, type: SnackBarType) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

fun View.showSnackWith(message: Int, type: SnackBarType) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
        .setBackgroundTint(resources.getColor(getColorWithType(type), null))
        .show()
}

/**
 * Avoid "IllegalArgumentException: No suitable parent found from the given view"
 * using root element of a view
 */
fun Fragment.showSafeSnackBar(message: Int, type: SnackBarType) {
    activity?.let {
        Snackbar.make(
            it.findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
            .setBackgroundTint(resources.getColor(getColorWithType(type), null))
            .show()
    }
}


fun Fragment.openBrowserWith(url: String?) {
    val i = Intent(Intent.ACTION_VIEW)
    var completeURL = url ?: ""
    if (!completeURL.startsWith("https://") && !completeURL.startsWith("http://")) {
        completeURL = "https://$completeURL"
    }
    i.data = Uri.parse(completeURL)
    startActivity(i)
}

private fun getColorWithType(type: SnackBarType): Int {
    return when (type) {
        SnackBarType.Success -> R.color.green
        SnackBarType.Error -> R.color.red_sold_out
        SnackBarType.Warning -> R.color.yellow_warning
    }
}

var TabLayout.tabsEnabled: Boolean
    get() {
        return tabsEnabled
    }
    set(value) {
        val tabStrip: LinearLayout = this.getChildAt(0) as LinearLayout
        tabStrip.children.forEach {
            it.setOnTouchListener { view, _ ->
                if (value) {
                    view.performClick()
                    false
                }
                true
            }
        }
    }

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun View.showKeyboard() {
    this.requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun <T> MutableLiveData<ArrayList<T>>.addAllValue(newlist: List<T>) {
    val list = this.value ?: arrayListOf()
    list.addAll(newlist)
    this.value = list
}

fun Button.enableButton(value: Boolean) {
    this.isEnabled = value
    this.setTextColor(if (value) Color.WHITE else Color.GRAY)
    val buttonDrawable = DrawableCompat.wrap(this.background)
    DrawableCompat.setTint(
        buttonDrawable, if (value) this.resources.getColor(R.color.pink_dark)
        else Color.DKGRAY
    )
    this.background = buttonDrawable

}

fun ImageView.load(url: String?, context: Context) {
    Glide.with(context)
        .load(url ?: "")
        .placeholder(R.drawable.beige_gradient_background)
        .error(R.drawable.pink_gradient_background)
        .into(this)
}

fun getMonthString(n: String): String {
    return when (n) {
        "01" -> "Jan"
        "02" -> "Fev"
        "03" -> "Mar"
        "04" -> "Abr"
        "05" -> "Mai"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Ago"
        "09" -> "Set"
        "10" -> "Out"
        "11" -> "Nov"
        "12" -> "Dez"
        else -> "?"
    }
}

fun getDayOfWeekBr(dateString: String): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("pt", "BR"))
    val date = dateFormat.parse(dateString)
    val calendar = Calendar.getInstance(Locale("pt", "BR"))
    calendar.time = date!!
    val dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale("pt", "BR"))
    val splittedDayName = dayName?.substring(0, 3)?.split("")?.toMutableList()!!
    splittedDayName[1] = splittedDayName[1].uppercase()
    return splittedDayName.joinToString("")
}

fun String.formatDateEvent(): String {
    val dateSplitted = this.split("-")
    return when (val day = dateSplitted[2]) {
        "01" -> "$day de Janeiro"
        "02" -> "$day de Fevereiro"
        "04" -> "$day de Abril"
        "05" -> "$day de Maio"
        "03" -> "$day de Março"
        "06" -> "$day de Junho"
        "07" -> "$day de Julho"
        "08" -> "$day de Agosto"
        "09" -> "$day de Setembro"
        "10" -> "$day de Outubro"
        "11" -> "$day de Novembro"
        "12" -> "$day de Dezembro"
        else -> "?"
    }
}

fun formatDateTrip(startDate: String, endDate: String): String {
    val sdSplitted = startDate.split("-")
    val sdDayOfWeek = getDayOfWeekBr(startDate)
    val sdDay = sdSplitted.last()
    val sdMonth = getMonthString(sdSplitted[1])
    val edSplitted = endDate.split("-")
    val edDayOfWeek = getDayOfWeekBr(endDate)
    val edDay = edSplitted.last()
    val edMonth = getMonthString(edSplitted[1])
    return "$sdDayOfWeek, $sdDay $sdMonth até $edDayOfWeek, $edDay $edMonth"
}

fun formatDateEvent(date: String, hour: String): String {
    val dateSplitted = date.split("-")
    val dayOfWeek = getDayOfWeekBr(date)
    val day = dateSplitted[2]
    val month = getMonthString(dateSplitted[1])
    return "$dayOfWeek, $day $month • $hour"
}

fun formatLocationTrip(locations: ArrayList<Location>): String =
    locations.map { it.name }.joinToString(", ")

fun formatDate(
    startDate: String,
    endDate: String
): String {
    val startDateSplitted = startDate.split("-").toList()
    val endDateSplitted = endDate.split("-").toList()
    return if (startDateSplitted[1] == endDateSplitted[1]) {
        "De ${startDateSplitted[2]} a ${endDateSplitted[2]} de ${
            getMonthString(
                startDateSplitted[1]
            )
        }"
    } else {
        "De ${startDateSplitted[2]} de ${getMonthString(startDateSplitted[1])} a ${endDateSplitted[2]} de ${
            getMonthString(
                endDateSplitted[1]
            )
        }"
    }
}

fun animateAlpha(view: View) {
    ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 400
        addUpdateListener {
            view.alpha = it.animatedValue as Float
        }
    }.start()
}

fun animateDirection(up: Boolean = false, down: Boolean = false, context: Context): Animation {
    return if (up) AnimationUtils.loadAnimation(
        context,
        R.anim.scale_up
    ) else AnimationUtils.loadAnimation(context, R.anim.scale_down)
}

fun ImageView.loadBlurry(url: String?, context: Context) {
    Glide.with(context)
        .load(url)
        .apply(RequestOptions.bitmapTransform(BlurTransformation(15, 3)))
        .into(this)
}




