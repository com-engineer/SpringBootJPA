package com.example.firebase_notes_api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class NoteService {

    private static final String COLLECTION_NAME = "notes";

    public String createNote(Map<String, Object> noteData) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document();

        ApiFuture<com.google.cloud.firestore.WriteResult> result =
                documentReference.set(noteData);

        result.get();
        return documentReference.getId();

    }

    public List<Map<String, Object>> getAllNotes() throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .get();

        QuerySnapshot querySnapshot = future.get();

        List<Map<String,Object>> notes = new ArrayList<>();

        for(DocumentSnapshot document:querySnapshot.getDocuments()){
            Map<String,Object> note = document.getData();

            if(note != null){
                note.put("id",document.getId());
                notes.add(note);
            }
        }

        return notes;

    }
}
