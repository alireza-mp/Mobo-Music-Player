package com.digimoplus.moboplayer.presentation.componnets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.digimoplus.moboplayer.presentation.theme.DarkGray
import com.digimoplus.moboplayer.presentation.theme.White

@Composable
fun CustomAlertDialog(
    openDialog: Boolean,
    onCancel: () -> Unit,
    onDelete: () -> Unit,
) {

    if (openDialog) {


        Dialog(onDismissRequest = onCancel) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 8.dp,
                backgroundColor = White,
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {

                    Text(
                        text = "Are you sure you want to delete this play list?",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 16.sp,
                        color = DarkGray,
                    )

                    Row(Modifier.padding(top = 10.dp)) {
                        OutlinedButton(
                            onClick = onCancel,
                            border = BorderStroke(1.dp, DarkGray),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(
                                text = "Cancel",
                                color = DarkGray,
                            )
                        }

                        Button(
                            onClick = onDelete,
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = DarkGray,
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F),
                        ) {
                            Text(
                                text = "Delete",
                                color = White,
                            )
                        }
                    }

                }
            }
        }

    }
}