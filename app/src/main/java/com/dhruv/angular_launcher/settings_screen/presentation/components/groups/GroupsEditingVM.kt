package com.dhruv.angular_launcher.settings_screen.presentation.components.groups

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.dhruv.angular_launcher.core.database.room.models.GroupData

class GroupsEditingVM(): ViewModel() {

    var showGroupEditingDialog      by mutableStateOf(false)
    var showGroupIconChoices         by mutableStateOf(false)
    var selectedGroup               by mutableStateOf<GroupData?>(null)
    var nameValue                   by mutableStateOf(TextFieldValue())
    var keyValue                    by mutableStateOf(TextFieldValue())

    fun getGroup (group: GroupData) {
        selectedGroup = group
        nameValue = TextFieldValue(group.name)
        keyValue = TextFieldValue(group.iconKey)
        showGroupEditingDialog = true
    }

    fun dismiss (){
        showGroupEditingDialog = false
    }

    fun save(
        addGroup: (GroupData) -> Unit,
    ) {
        if (selectedGroup != null){
            addGroup(selectedGroup!!.copy(name = nameValue.text, iconKey = keyValue.text))
        }
        selectedGroup = null
        showGroupEditingDialog = false
    }
}