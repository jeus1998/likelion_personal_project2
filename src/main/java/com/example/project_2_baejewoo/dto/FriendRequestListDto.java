package com.example.project_2_baejewoo.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FriendRequestListDto {

    private List<FriendRequest> friendRequests = new ArrayList<>();

    public void addFriendRequest(Long id, String username, String request) {
        FriendRequest friendRequest = new FriendRequest(id, username, request);
        friendRequests.add(friendRequest);
    }

    @Data
    private static class FriendRequest {
        private Long id;
        private String username;
        private String request;

        public FriendRequest(Long id, String username, String request) {
            this.id = id;
            this.username = username;
            this.request = request;
        }
    }
}
