package com.example.present.data

object StringProvider {
    const val API = "c375e61a-5a3f-44eb-96b5-53a3c5a7b8b6"

    const val ON_BACK_PRESSED_MESSAGE = "Нажми еще раз для выхода"

    // Dialog region
    const val DIALOG_ERROR_MESSAGE = "Не угадал или неправильно ввел)"

    const val POSITIVE_DIALOG_BUTTON = "Сейчас"
    const val NEGATIVE_DIALOG_BUTTON = "Потом"
    const val POSITIVE_ADDITIONAL_BUTTON = "Оки^^"
    const val DIALOG_UNDERSTAND_BUTTON = "Понял("
    const val GO = "Вперед!"

    const val DIALOG_CORRECT_TAG = "correct_dialog"
    const val DIALOG_ADDITIONAL_TAG = "additional_dialog"
    const val DIALOG_ERROR_TAG = "error_dialog"
    const val DIALOG_GO_TAG = "go_dialog"
    // Dialog region

    // Get Present region
    const val REDIRECT_WARNING =
        "Код активации скопирован, нажми \"Вперед!\" и тебя перенесет на страничку для активации)"
    const val OZON_CERTIFICATE_KEY = "123"
    const val OZON_REDIRECT_URL = "https://www.ozon.ru/my/codes"
    const val COPY_FINISHED = "Код скопирован!"
    // Get Present region

    const val COUNTER_SCHEMA = "MMмес:ddдней:hhчас:mman:ssse"

    // Hint region
    val dialogCongratulationTitleMap: Map<Int, String> = mapOf(
        0 to "Поздравляю тебя с первым твоим подарочком\nОткроешь сейчас или потом?",
        1 to "Ого, да ты уже добралась до второго, поздравляю^^\nОткроешь сейчас или потом?",
        2 to "Ты дошла до последнего подарка в этом приложении\nОткроешь сейчас или потом?"
    )

    val hintMap = mapOf(
        0 to "Первая подсказка",
        1 to "Вторая подсказка",
        2 to "Третья подсказка",
        3 to "Четвертая подсказка"
    )

    val additionalHintMap = mapOf(
        0 to "Первая дополнительная подсказка",
        1 to "Вторая дополнительная подсказка",
        2 to "Третья дополнительная подсказка",
        3 to "Четвертая дополнительная подсказка"
    )
    // Hint region

    val presentNameList = listOf(
        "Твой первый подарок",
        "Твой второй подарок",
        "Твой третий подарок"
    )
}