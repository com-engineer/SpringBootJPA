package com.example.firebase_notes_api.service;

import com.example.firebase_notes_api.dto.CreateNotesDto;
import com.example.firebase_notes_api.dto.NoteResponseDto;
import com.example.firebase_notes_api.exception.FirebaseOperationException;
import com.example.firebase_notes_api.exception.NoteNotFoundException;
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

    public NoteResponseDto createNote(CreateNotesDto dto) {

        try{
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

            if(!documentSnapshot.exists()){
                throw new FirebaseOperationException( "Note was created but could not be retrieved");
            }

            return map(documentSnapshot);
        }catch (ExecutionException e) {
            throw new FirebaseOperationException("fail to create notes");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FirebaseOperationException("Note Creation was interrupted");
        }

    }

    public List<NoteResponseDto> getAllNotes() {

        try{
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
        }catch (ExecutionException e) {
            throw new FirebaseOperationException("fail to fetch notes");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FirebaseOperationException("Notes fetching was interrupted");
        }



    }

    public NoteResponseDto getNote(String id) {

        try{
            Firestore firestore = FirestoreClient.getFirestore();

            DocumentReference documentReference = firestore
                    .collection(COLLECTION_NAME)
                    .document(id);

            ApiFuture<DocumentSnapshot> future = documentReference.get();

            DocumentSnapshot document = future.get();

            if(!document.exists()){
                throw new NoteNotFoundException("Note does not exists with the id: "+id);
            }

            return map(document);
        }catch (ExecutionException e) {
            throw new FirebaseOperationException("fail to fetch note");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FirebaseOperationException("Note fetching was interrupted");
        }


    }

    public NoteResponseDto updateNote(String id, CreateNotesDto noteDto) {

        try{
            Firestore firestore = FirestoreClient.getFirestore();

            DocumentReference documentReference = firestore
                    .collection(COLLECTION_NAME)
                    .document(id);

            DocumentSnapshot document = documentReference.get().get();
            if(!document.exists()){
                throw new NoteNotFoundException("Note does not exists with the id: "+id);
            }

            Map<String,Object> noteData = new HashMap<>();
            noteData.put("title",noteDto.getTitle());
            noteData.put("content",noteDto.getContent());

            ApiFuture<WriteResult> result = documentReference.update(noteData);

            result.get();

            DocumentSnapshot updatedDocument = documentReference.get().get();

            if(!updatedDocument.exists()){
                throw new FirebaseOperationException("Note was updated but could not be retrieved");
            }

            return map(document);
        } catch (ExecutionException e) {
            throw new FirebaseOperationException("fail to update note");
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FirebaseOperationException("Note update was interrupted");
        }

    }

    public void deleteNote(String id) {

        try{
            Firestore firestore = FirestoreClient.getFirestore();

            DocumentReference documentReference = firestore
                    .collection(COLLECTION_NAME)
                    .document(id);
            DocumentSnapshot document = documentReference.get().get();
            if(!document.exists()){
                throw new NoteNotFoundException("Note does not exists with the id: "+id);
            }
            documentReference.delete().get();
        }catch (ExecutionException e) {
            throw new FirebaseOperationException("fail to delete the note",e);
        }catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new FirebaseOperationException("Note Deleting was interrupted");
        }

    }
}
