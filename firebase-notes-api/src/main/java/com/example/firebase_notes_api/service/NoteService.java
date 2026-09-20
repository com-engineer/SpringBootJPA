package com.example.firebase_notes_api.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class NoteService {

    private static final String COLLECTION_NAME = "notes";

    public String createNote(Map<String, Object> noteData) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        //debug//
        log.info("output of firestore: {}", firestore);
        log.info("note data that has to be created: {}", noteData);
        //debug//

        noteData.put("createdAt", FieldValue.serverTimestamp());
        //debug//
        log.info("noteData after adding timestamp: {}", noteData);
        //debug//

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document();
        //debug//
        log.info("output of documentReference: {}", documentReference);
        //debug//

        ApiFuture<com.google.cloud.firestore.WriteResult> result =
                documentReference.set(noteData);

        //debug//
        log.info("output of result: {}", result);
        //debug//

        result.get();
        //debug//
        log.info("output of result.get(): {}", result.get());
        //debug//
        return documentReference.getId();

    }

    public List<Map<String, Object>> getAllNotes() throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .get();
        //debug//
        log.info("output of future: {}", future);
        //debug//

        QuerySnapshot querySnapshot = future.get();
        //debug//
        log.info("output of querySnapshot: {}", querySnapshot);
        //debug//

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

    public Map<String, Object> getNote(String id) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .get();
        QuerySnapshot querySnapshot = future.get();

        Map<String,Object> note = new HashMap<>();

        for(DocumentSnapshot document:querySnapshot.getDocuments()){
            if(document.getId().equals(id)){
                note = document.getData();
                break;
            }
        }

        return note;

    }

    public Map<String, Object> updateNote(String id, Map<String, Object> noteDto) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document(id);
        noteDto.put("createdAt",FieldValue.serverTimestamp());
        ApiFuture<WriteResult> result = documentReference.update(noteDto);

        result.get();

        DocumentSnapshot document = documentReference.get().get();

        if(document != null){
            return document.getData();
        }

        return null;
    }

    public void deleteNote(String id) {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document(id);

        documentReference.delete();
    }
}
