package org.example;

import java.util.ArrayList;
import java.util.List;

public class Transport {
    private String ufrag;
    private String pwd;
    private String fingerprint;
    private final List<Candidate> candidates = new ArrayList<>();

    // Getter and setter methods
    public String getUfrag() { return ufrag; }
    public void setUfrag(String ufrag) { this.ufrag = ufrag; }

    public String getPwd() { return pwd; }
    public void setPwd(String pwd) { this.pwd = pwd; }

    public List<Candidate> getCandidates() { return candidates; }
    public void addCandidate(Candidate candidate) { this.candidates.add(candidate); }
    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }
    public String getFingerprint() {
        return fingerprint;
    }
    public boolean hasCandidates() {
        return !candidates.isEmpty();
    }
}


