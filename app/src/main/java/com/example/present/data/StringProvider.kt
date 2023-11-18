package com.example.present.data

object StringProvider {
    const val API = "c375e61a-5a3f-44eb-96b5-53a3c5a7b8b6"
    const val APP_DEEPLINK_BASE = "present://distributive"
    const val APP_DEEPLINK_MAIN = "/main"
    const val ADD_CODE = "&open_code="
    const val QR_TOAST = "QR код был сохранен в Download"

    const val ON_BACK_PRESSED_MESSAGE = "Нажми еще раз для выхода"

    // Dialog region
    const val DIALOG_ERROR_MESSAGE = "Не угадал или неправильно ввел)"

    const val POSITIVE_DIALOG_BUTTON = "Сейчас"
    const val NEGATIVE_DIALOG_BUTTON = "Потом"
    const val POSITIVE_ADDITIONAL_BUTTON = "Оки^^"
    const val DIALOG_UNDERSTAND_BUTTON = "Понял("
    const val GO = "Вперед!"
    const val CONTINUE = "Продолжить"
    const val EXIT = "Выйти"
    const val DONE = "Готово"
    const val NEXT = "Далее"
    const val YES = "Да"
    const val NO = "Нет"

    const val ERROR_SAVE_INFO_ITEM = "Не удалось сохранить данные, проверьте их кореектность"

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

    const val AGREE = "Понятно"
    const val FORM_MESSAGE = "Приветствую в создателе квеста. Здесь вы можете создать свою игру. " +
            "Нажмите на + чтобы добавить подарок. Когда вы введете все подарки, " +
            "то их можно отправить, нажав на соответствующую кнопку"

    const val EXIT_FORM_REDACTOR = "Вы действительно хотите прекратить создание подарка? Прогресс не сохранится"
    const val COMPLETE_FORM_REDACTOR = "Сохранить подарок?"
    const val POINT_FORM_ERROR = "Не все поля заполнены"

    const val ERROR_LATITUDE_POINT = "Необходимо заполнить долготу"
    const val ERROR_LONGITUDE_POINT = "Необходимо заполнить широту"
    const val ERROR_TEXT_POINT = "Необходимо заполнить текст"
    const val ERROR_HINT_POINT = "Необходимо заполнить подсказку"
    const val ERROR_CONGRATULATION_PRESENT = "Необходимо заполнить поздравление"
    const val ERROR_LINK_PRESENT = "Необходимо заполнить ссылку на активацию"
    const val ERROR_KEY_PRESENT = "Необходимо заполнить ключ для активации подарка"
}