package infrastructure.audio;

import entities.Track;

import java.util.*;

public class PlaybackQueue {
    private final Deque<Track> trackQueue = new ArrayDeque<>();
    private final Deque<Track> history = new ArrayDeque<>();
    private final List<Track> originalOrder = new ArrayList<>();
    private final Set<Track> trackSet = new HashSet<>();
    private Track currentTrack;
    private boolean shuffle = false;
    private boolean loopQueue = false;

    public void add(Track track) {
        if (track == null) return;
        trackQueue.add(track);
        originalOrder.add(track);
    }

    public void addAll(List<Track> tracks) {
        if (tracks == null || tracks.isEmpty()) return;
        for (Track t : tracks) {
            if (trackSet.add(t)) {
                trackQueue.add(t);
                originalOrder.add(t);
            }
        }
    }
    public void clear(){
        trackQueue.clear();
        history.clear();
        originalOrder.clear();
        currentTrack = null;
    }

    public Track next() {
        if (trackQueue.isEmpty()) {
            if (loopQueue) {
                resetQueue(); // repopulate the queue from originalOrder
            } else {
                return null;
            }
        }

        if (!trackQueue.isEmpty()) {
            if (currentTrack != null) {
                history.push(currentTrack);
            }
            currentTrack = trackQueue.poll();
        }

        return currentTrack;
    }
    public Track previous() {
        if (!history.isEmpty()) {
            if (currentTrack != null) {
                trackQueue.addFirst(currentTrack);
            }
            currentTrack = history.pop();
            return currentTrack;
        }
        return null;
    }
    public Track peekNext() {
        return trackQueue.peek();
    }

    public boolean isEmpty(){
        return trackQueue.isEmpty();
    }

    public void shuffle() {
        List<Track> tempList = new ArrayList<>(trackQueue);
        Collections.shuffle(tempList);
        trackQueue.clear();
        trackQueue.addAll(tempList);
    }

    public boolean remove(Track track) {
        boolean removed = trackQueue.remove(track);
        originalOrder.remove(track);
        history.remove(track);
        trackSet.remove(track);

        if (track.equals(currentTrack)) {
            currentTrack = null;
        }

        return removed;
    }

    public Track getCurrentTrack() {
        return currentTrack;
    }

    public void setShuffle(boolean enable) {
        if (shuffle == enable) return;

        shuffle = enable;
        resetQueue();
    }

    public boolean isShuffleEnabled() {
        return shuffle;
    }

    public List<Track> getHistory() {
        return new ArrayList<>(history);
    }

    public void setLoopQueue(boolean enable) {
        loopQueue = enable;
    }

    public boolean isLoopQueueEnabled() {
        return loopQueue;
    }

    private void resetQueue() {
        Track oldCurrent = currentTrack;

        trackQueue.clear();
        history.clear();

        List<Track> temp = new ArrayList<>(originalOrder);
        if (shuffle) {
            Collections.shuffle(temp);
        }

        if (oldCurrent != null) {
            temp.remove(oldCurrent);
        }

        trackQueue.addAll(temp);

        if (oldCurrent != null) {
            currentTrack = oldCurrent;
        }
    }

}
