/*
package com.example.personaltaskmanager
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FetchEventsFromFirestore() {
    val events = remember { mutableStateListOf<Events>() }
    val db = FirebaseFirestore.getInstance()
    val eventsCollection = db.collection("events")

    LaunchedEffect(Unit) {
        eventsCollection.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val event = document.toObject(Events::class.java)
                    events.add(event)
                }
            }
            .addOnFailureListener { e ->
                println("Error: $e")
            }
    }

    LazyColumn {
        items(events) { event ->
            Text(text = event.event_name)
            Text(text = event.location)
            Text(text = "${event.event_date} ${event.event_time}")
        }
    }
}

fun updateNoteInFirestore(noteId: String, newNote: Note) {
    val noteRef = firestore.collection("notes").document(noteId)
    noteRef.set(newNote)
        .addOnSuccessListener {
            // Successfully updated
        }
        .addOnFailureListener { exception ->
            // Handle error
        }
}*/
