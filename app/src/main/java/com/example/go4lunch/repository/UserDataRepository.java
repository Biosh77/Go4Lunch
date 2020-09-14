package com.example.go4lunch.repository;

import com.example.go4lunch.models.Workmate;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserDataRepository {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE --- //

    public static CollectionReference getUserCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE --- //

    public static Task<Void> createUser(String uid, String username, String urlPicture) {
        Workmate workmateToCreate = new Workmate(uid, username, urlPicture);
        return UserDataRepository.getUserCollection().document(uid).set(workmateToCreate);
    }

    // --- GET --- //

    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserDataRepository.getUserCollection().document(uid).get();
    }

    
}
