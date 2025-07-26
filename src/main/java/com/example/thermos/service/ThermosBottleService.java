package com.example.thermos.service;

import com.example.thermos.model.ThermosBottle;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ThermosBottleService {
    
    private static final String COLLECTION_NAME = "thermos_bottles";
    
    private Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }

    // Lấy tất cả bình giữ nhiệt
    public List<ThermosBottle> getAllThermosBottles() {
        try {
            Firestore dbFirestore = getFirestore();
            ApiFuture<QuerySnapshot> future = dbFirestore.collection(COLLECTION_NAME).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            
            List<ThermosBottle> thermosBottles = new ArrayList<>();
            for (QueryDocumentSnapshot document : documents) {
                ThermosBottle bottle = document.toObject(ThermosBottle.class);
                bottle.setId(document.getId());
                thermosBottles.add(bottle);
            }
            return thermosBottles;
        } catch (Exception e) {
            System.err.println("Error getting thermos bottles: " + e.getMessage());
            return new ArrayList<>(); // Return empty list if error occurs
        }
    }

    // Lấy bình giữ nhiệt theo ID
    public ThermosBottle getThermosBottleById(String id) {
        try {
            Firestore dbFirestore = getFirestore();
            DocumentReference documentReference = dbFirestore.collection(COLLECTION_NAME).document(id);
            ApiFuture<DocumentSnapshot> future = documentReference.get();
            DocumentSnapshot document = future.get();
            
            if (document.exists()) {
                ThermosBottle bottle = document.toObject(ThermosBottle.class);
                bottle.setId(document.getId());
                return bottle;
            }
        } catch (Exception e) {
            System.err.println("Error getting thermos bottle by ID: " + e.getMessage());
        }
        return null;
    }

    // Thêm mới bình giữ nhiệt
    public String saveThermosBottle(ThermosBottle thermosBottle) {
        try {
            Firestore dbFirestore = getFirestore();
            thermosBottle.setCreatedAt(LocalDateTime.now());
            thermosBottle.setUpdatedAt(LocalDateTime.now());
            
            ApiFuture<DocumentReference> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME).add(thermosBottle);
            DocumentReference documentReference = collectionsApiFuture.get();
            return documentReference.getId();
        } catch (Exception e) {
            System.err.println("Error saving thermos bottle: " + e.getMessage());
            return null;
        }
    }

    // Cập nhật bình giữ nhiệt
    public String updateThermosBottle(String id, ThermosBottle thermosBottle) {
        try {
            Firestore dbFirestore = getFirestore();
            thermosBottle.setUpdatedAt(LocalDateTime.now());
            
            ApiFuture<WriteResult> collectionsApiFuture = dbFirestore.collection(COLLECTION_NAME)
                    .document(id).set(thermosBottle);
            collectionsApiFuture.get();
            return id;
        } catch (Exception e) {
            System.err.println("Error updating thermos bottle: " + e.getMessage());
            return null;
        }
    }

    // Xóa bình giữ nhiệt
    public boolean deleteThermosBottle(String id) {
        try {
            Firestore dbFirestore = getFirestore();
            ApiFuture<WriteResult> writeResult = dbFirestore.collection(COLLECTION_NAME).document(id).delete();
            writeResult.get();
            return true;
        } catch (Exception e) {
            System.err.println("Error deleting thermos bottle: " + e.getMessage());
            return false;
        }
    }

    // Tìm kiếm bình giữ nhiệt theo tên hoặc thương hiệu
    public List<ThermosBottle> searchThermosBottles(String keyword) {
        try {
            Firestore dbFirestore = getFirestore();
            List<ThermosBottle> results = new ArrayList<>();
            
            // Tìm kiếm theo tên
            ApiFuture<QuerySnapshot> nameQuery = dbFirestore.collection(COLLECTION_NAME)
                    .whereGreaterThanOrEqualTo("name", keyword)
                    .whereLessThanOrEqualTo("name", keyword + "\uf8ff")
                    .get();
            
            List<QueryDocumentSnapshot> nameDocuments = nameQuery.get().getDocuments();
            for (QueryDocumentSnapshot document : nameDocuments) {
                ThermosBottle bottle = document.toObject(ThermosBottle.class);
                bottle.setId(document.getId());
                results.add(bottle);
            }
            
            // Tìm kiếm theo thương hiệu
            ApiFuture<QuerySnapshot> brandQuery = dbFirestore.collection(COLLECTION_NAME)
                    .whereGreaterThanOrEqualTo("brand", keyword)
                    .whereLessThanOrEqualTo("brand", keyword + "\uf8ff")
                    .get();
            
            List<QueryDocumentSnapshot> brandDocuments = brandQuery.get().getDocuments();
            for (QueryDocumentSnapshot document : brandDocuments) {
                ThermosBottle bottle = document.toObject(ThermosBottle.class);
                bottle.setId(document.getId());
                // Tránh trùng lặp
                if (!results.stream().anyMatch(b -> b.getId().equals(bottle.getId()))) {
                    results.add(bottle);
                }
            }
            
            return results;
        } catch (Exception e) {
            System.err.println("Error searching thermos bottles: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}