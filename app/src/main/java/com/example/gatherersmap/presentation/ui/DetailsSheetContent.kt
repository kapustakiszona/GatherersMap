package com.example.gatherersmap.presentation.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ElevatedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gatherersmap.domain.model.ItemSpot

@Composable
fun DetailsSheetContent(
    itemSpot: ItemSpot,
    onEditClickListener: (ItemSpot) -> Unit
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp),

        ) {
        Text(text = "${itemSpot.name}  id:${itemSpot.id}", fontSize = 26.sp)
        Text(
            text = itemSpot.description,
            fontSize = 16.sp,
            color = MaterialTheme.colors.onBackground
        )
        Spacer(Modifier.height(10.dp))
        Image(
            modifier = Modifier.clip(RoundedCornerShape(10.dp)).size(200.dp),
            painter = painterResource(id = itemSpot.image),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        ElevatedButton(
            onClick = {
                onEditClickListener(itemSpot)
            },
            contentPadding = PaddingValues(start = 14.dp, end = 14.dp),
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(text = "Edit")
        }
    }
}