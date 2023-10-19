package com.example.present.data

object StringProvider {
    const val API = "c375e61a-5a3f-44eb-96b5-53a3c5a7b8b6"

    const val ON_BACK_PRESSED_MESSAGE = "Нажми еще раз для выхода"

    // Dialog region
    const val DIALOG_ERROR_MESSAGE = "Не угадала или неправильно ввела)"

    const val POSITIVE_DIALOG_BUTTON = "Сейчас"
    const val NEGATIVE_DIALOG_BUTTON = "Потом"
    const val POSITIVE_ADDITIONAL_BUTTON = "Оки^^"
    const val DIALOG_UNDERSTAND_BUTTON = "Поняла("
    const val GO = "Вперед!"

    const val DIALOG_CORRECT_TAG = "correct_dialog"
    const val DIALOG_ADDITIONAL_TAG = "additional_dialog"
    const val DIALOG_ERROR_TAG = "error_dialog"
    const val DIALOG_GO_TAG = "go_dialog"
    // Dialog region

    // Get Present region
    const val REDIRECT_WARNING =
        "Код активации скопирован, нажми \"Вперед!\" и тебя перенесет на страничку для активации)"

    private const val OZON_CERTIFICATE_KEY = "8R8X-KSRD-KNM"
    private const val OZON_REDIRECT_URL = "https://www.ozon.ru/my/codes"

    private const val YANDEX_REDIRECT_URL = "https://plus.yandex.ru/gift"
    private const val YANDEX_CERTIFICATE_KEY = "F8CX2CEE86"

    private const val GOLDEN_APPLE_GIFT_URL = "https://ga.gift/ru/ecb93848b53e48009a9640eaec3c2fae"

    val redirectUrls: Map<Int, String> = mapOf(
        0 to OZON_REDIRECT_URL,
        1 to YANDEX_REDIRECT_URL,
        2 to GOLDEN_APPLE_GIFT_URL
    )

    val keyMap: Map<Int, String> = mapOf(
        0 to OZON_CERTIFICATE_KEY,
        1 to YANDEX_CERTIFICATE_KEY,
        2 to ""
    )

    val congratulationsWithPresent: Map<Int, String> = mapOf(
        0 to "Ты открыла свой первый подарок, поздравляю тебя, душа моя!\nНадеюсь на озоне ты себе прикупишь что-нибудь интересное^^",
        1 to "Ого-ого это что? Второй подарок? Получается еще 6 месяцев использовать Яндекс?!?!?",
        2 to "Она слишком много говорила мне о золотом яблоке, ну что ж, третий подарочек для тебя тут)"
    )

    const val COPY_FINISHED = "Код скопирован!"
    // Get Present region

    const val COUNTER_SCHEMA = "MMмес:ddдней:hhчас:mman:ssse"

    // Hint region
    val dialogCongratulationTitleMap: Map<Int, String> = mapOf(
        0 to "Поздравляю тебя с первым твоим подарочком, Ксю❤️\nОткроешь сейчас или потом?",
        1 to "Ого, да ты уже добралась до второго, поздравляю^^❤️\nОткроешь сейчас или потом?",
        2 to "Ты дошла до последнего подарка в этом приложении, любимка❤️\nОткроешь сейчас или потом?"
    )

    val hintMap = mapOf(
        0 to "Первый код ждет тебя прямо у моего дома)",
        1 to "Лунопарк:\n1 - количество аттракиуонов в лунопарке\n2 - стоимость билета на гусеницу",
        2 to "Вэйп-шоп в котором я покупал тебе картриджи:\nВведи транспорт от этой остановки до твоего пункта на первомайской.\nСначала троллейбусы, а потом маршрутки",
        3 to "Вечный огонь"
    )

    val additionalHintMap = mapOf(
        0 to "Он написан на бордюре",
        1 to "Всю информацию ты можешь легко найти на кассе^^",
        2 to "Все маршруты ты можешь посмотреть на картах(7П и 3П не включам, они прямо до тебя не едет)",
        3 to "Там такооой красивый мальчик высооокий будет))))))"
    )
    // Hint region

    val presentNameList = listOf(
        "Твой первый подарок ❤️",
        "Твой второй подарок ❤️",
        "Твой третий подарок ❤️"
    )
}