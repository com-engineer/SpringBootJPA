package com.example.firebase_notes_api.service;

import com.example.firebase_notes_api.dto.CreateNotesDto;
import com.example.firebase_notes_api.dto.NoteResponseDto;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class NoteService {

    private static final String COLLECTION_NAME = "notes";

    public NoteResponseDto map(DocumentSnapshot data){

        Timestamp timestamp = data.getTimestamp("createdAt");

        Instant createdAt =  timestamp != null
                ? timestamp.toDate().toInstant()
                : null;

        return new NoteResponseDto(
                data.getId(),
                data.getString("title"),
                createdAt,
                data.getString("content")
        );
    }

    public NoteResponseDto createNote(CreateNotesDto dto) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        Map<String,Object> noteData = new HashMap<>();
        noteData.put("title",dto.getTitle());
        noteData.put("createdAt", FieldValue.serverTimestamp());
        noteData.put("content",dto.getContent());

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document();

        ApiFuture<WriteResult> result =
                documentReference.set(noteData);

        result.get();

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot documentSnapshot = future.get();

        return map(documentSnapshot);

    }

    public List<NoteResponseDto> getAllNotes() throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = firestore
                .collection(COLLECTION_NAME)
                .get();

        QuerySnapshot querySnapshot = future.get();


        List<NoteResponseDto> notes = new ArrayList<>();

        for(DocumentSnapshot document:querySnapshot.getDocuments()){
            NoteResponseDto note = map(document);

            if(note != null){
                notes.add(note);
            }
        }

        return notes;

    }

    public NoteResponseDto getNote(String id) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document(id);

        ApiFuture<DocumentSnapshot> future = documentReference.get();

        DocumentSnapshot document = future.get();

        if(document!=null){
            return map(document);
        }

        return null;

    }

    public NoteResponseDto updateNote(String id, CreateNotesDto noteDto) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document(id);

        Map<String,Object> noteData = new HashMap<>();
        noteData.put("title",noteDto.getTitle());
        noteData.put("content",noteDto.getContent());

        ApiFuture<WriteResult> result = documentReference.update(noteData);

        result.get();

        DocumentSnapshot document = documentReference.get().get();

        if(document.exists()){
            return map(document);
        }

        return null;
    }

    public void deleteNote(String id) throws ExecutionException, InterruptedException {

        Firestore firestore = FirestoreClient.getFirestore();

        DocumentReference documentReference = firestore
                .collection(COLLECTION_NAME)
                .document(id);

        documentReference.delete().get();
    }
}
