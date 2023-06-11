package com.example.gatherersmap.presentation.permissionshandling

sealed class PermissionsDialogState {
    object Initial : PermissionsDialogState()

    object MainDialog : PermissionsDialogState()

    object DenyPermissionsDialog : PermissionsDialogState()

    object StartWithoutPermissionDialog : PermissionsDialogState()

}
/**
 *1 главный диалог запускается
 *2 жмем инейбл
 *3 жмем отказаться от пермишенов
 *4 shouldShowRationale становится Тру
 *5 срабатывает Иф и вызывается ПервыЙ АлертДиалогКомпонент
 *6 жмем кнопку на диалоге и вызываем еще раз окно выбора пермишенов
 *7 жмем второй раз отменить
 *8 ШудШоуРейшнл становится Фолс
 *9 Вызывается Второй АлертДиалог компонент с предложением начать без пермишена
 *10 жмем Начать без пермишенов и скрываем диалог
 */