package com.example.go4lunch.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

public class UserDataRepository {

    private static final String COLLECTION_NAME = "users";

    public MutableLiveData<List<Workmate>> GetWorkmates() {
        MutableLiveData<List<Workmate>> workmates = new MutableLiveData<>();
        List<Workmate> listWorkmates = new ArrayList<>();

        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Workmate workmate = document.toObject(Workmate.class);
                        listWorkmates.add(workmate);
                    }
                    workmates.setValue(listWorkmates);
                }
            }
        });
        return workmates;
    }

    // --- COLLECTION REFERENCE --- //

    public static CollectionReference getUserCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE --- //

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        Workmate workmateToCreate = new Workmate(uid, username, urlPicture);
        return UserDataRepository.getUserCollection().document(uid).set(workmateToCreate);
    }



    // --- UPDATE --- //

    public static Task<Void> updateUser(String uid, String username, String urlPicture) {
        return UserDataRepository.getUserCollection().document(uid).update("username", username, "urlPicture", urlPicture);
    }

    public void updateLikes(String uid, List<String> likes) {

        DocumentReference RefLikes = getUserCollection().document(uid);

        RefLikes.update("likes", likes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });
    }


    public void updateChoice(String uid, String interestedBy) {


        DocumentReference RefChoice = getUserCollection().document(uid);

        RefChoice.update("interestedBy", interestedBy).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });
    }


    public void updateVicinity(String uid, String vicinity) {

        DocumentReference RefChoice = getUserCollection().document(uid);

        RefChoice.update("vicinity", vicinity).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
            }
        });
    }

    public static Task<DocumentSnapshot> getWorkmate(String uid) {
        return UserDataRepository.getUserCollection().document(uid).get();
    }

    public static Task<QuerySnapshot> getWorkmatesWhoHaveSameChoice(String interestedBy) {
        return getUserCollection().whereEqualTo("interestedBy", interestedBy).get();
    }

    public static FirebaseUser getCurrentUser(){return FirebaseAuth.getInstance().getCurrentUser();}

}
