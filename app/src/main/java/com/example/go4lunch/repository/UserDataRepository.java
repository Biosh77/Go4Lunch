package com.example.go4lunch.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.go4lunch.models.Workmate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserDataRepository {

    private static final String COLLECTION_NAME = "users";

    public MutableLiveData<List<Workmate>> GetWorkmates(){
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


    //public static FirebaseUser getCurrentUser() {
      //  return FirebaseAuth.getInstance().getCurrentUser();
    //}

    //public static Task<Void> createLike(){
      //  return ;
    //}
}
