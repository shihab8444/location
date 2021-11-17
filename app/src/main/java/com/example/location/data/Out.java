package com.example.location.data;

public class Out {
    private String id,token,email;
    private boolean location_updates;

    private Point lastLocation;

    public Out() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isLocation_updates() {
        return location_updates;
    }

    public void setLocation_updates(boolean location_updates) {
        this.location_updates = location_updates;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Point getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Point lastLocation) {
        this.lastLocation = lastLocation;
    }
}
