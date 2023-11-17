(ns advent-of-code.day22)

(def ^:private spells
  {:M {:name "Magic Missile", :cost 53, :damage 4}
   :D {:name "Drain", :cost 73, :damage 2, :health 2}
   :S {:name "Shield", :cost 113, :effect 6, :armor 7}
   :P {:name "Poison", :cost 173, :damage 3, :effect 6}
   :R {:name "Recharge", :cost 229, :mana 101, :effect 5}})

;; Will always evaluate spells in ascending order of cost:
(def ^:private sorted-spells
  (sort-by #(get-in spells [% :cost]) (keys spells)))

;; Simple cost-table for spells, for easier look-ups later:
(def ^:private cost (into {}
                          (map hash-map sorted-spells
                               (map #(get-in spells [% :cost]) sorted-spells))))

;; Used to signal pruning of search tree branches:
(def ^:private sentinel Integer/MAX_VALUE)

;; Simple predicate for testing whether player or boss have died:
(def ^:private dead? (comp not pos? :hp))

(defn- boss-stats [data]
  ;; Nothing fancy. Line 1 is HP, 2 is damage.
  (let [nums (map #(Integer/parseInt %) (re-seq #"\d+" data))]
    (into {} (map hash-map [:hp :damage] nums))))

(defn- add-buff [state spell]
  (let [time (get-in spells [spell :effect])
        buff (case spell
               :S {:timer time, :armor (get-in spells [:S :armor])}
               :P {:timer time, :damage (get-in spells [:P :damage])}
               :R {:timer time, :mana (get-in spells [:R :mana])}
               ;; No effect for this spell
               nil)]
    (if buff (assoc-in state [:e spell] buff) state)))

;; Remove the indicated buff if its :timer is 0
(defn- clear-if-zero [state key]
  (if (zero? (get-in state [:e key :timer]))
    (update state :e dissoc key)
    state))

(defn- apply-poison [state]
  (if-let [poison (get-in state [:e :P])]
    (-> state
        ;; Apply the poison damage
        (update-in [:b :hp] - (poison :damage))
        ;; Decrement the timer
        (update-in [:e :P :timer] dec)
        ;; Remove the buff if timer is 0
        (clear-if-zero :P))
    state))

(defn- apply-recharge [state]
  (if-let [recharge (get-in state [:e :R])]
    (-> state
        ;; Apply the recharge buff
        (update-in [:p :mana] + (recharge :mana))
        ;; Decrement the timer
        (update-in [:e :R :timer] dec)
        ;; Remove the buff if timer is 0
        (clear-if-zero :R))
    state))

(defn- lower-shield [state]
  (if-let [_ (get-in state [:e :S])]
    (-> state
        ;; No permanent change for shield. Just decrement the timer.
        (update-in [:e :S :timer] dec)
        ;; Remove the buff if timer is 0
        (clear-if-zero :S))
    state))

(defn- apply-buffs [state]
  (->> state apply-poison apply-recharge lower-shield))

(defn- cast-spell [spell state]
  (let [cost  (get-in spells [spell :cost])
        state (update-in state [:p :mana] - cost)]
    (if (get-in spells [spell :effect])
      ;; Spells with :effect add buffs but don't do anything else this turn.
      (add-buff state spell)
      ;; The others do things immediately
      (let [newhp (min 50 (+ (get-in state [:p :hp])
                             (get-in spells [spell :health] 0)))]
        (assoc-in (update-in state [:b :hp] - (get-in spells [spell :damage]))
                  [:p :hp] newhp)))))

(defn- player-turn [spell state]
  (let [new-state (update-in (apply-buffs state) [:p :hp] (:f state))]
    (cond
      (dead? (:p state))            nil
      (>= (get-in state [:p :mana])
          (cost spell))             (cast-spell spell new-state)
      :else                         nil)))

(defn- boss-turn [state]
  (if (nil? state)
    nil
    (let [armor     (get-in state [:e :S :armor] 0)
          new-state (apply-buffs state)]
      (if (pos? (get-in new-state [:b :hp]))
        (update-in new-state [:p :hp]
                   - (max (- (get-in new-state [:b :damage]) armor) 1))
        new-state))))

(defn- one-pair [state spell]
  (->> state
       (player-turn spell)
       boss-turn))

(defn- fight-with-depth [depth state used]
  (cond
    (nil? state)       sentinel
    (dead? (:b state)) 0
    (dead? (:p state)) sentinel
    (zero? depth)      sentinel
    :else
    (apply min (map #(+ (cost %)
                        (fight-with-depth (dec depth)
                                          (one-pair state %)
                                          used))
                    sorted-spells))))

(defn- best-fight [hard hp mana boss max-depth]
  (let [f     (if hard dec identity)
        state {:p {:hp hp, :mana mana}, :b boss, :e {}, :f f}]
    (fight-with-depth max-depth state 0)))

(defn- fight [hard hp mana boss]
  (first (filter #(< % sentinel)
                 (for [max-depth (rest (range))]
                   (best-fight hard hp mana boss max-depth)))))

(defn part-1
  "Day 22 Part 1"
  [input]
  (->> input
       boss-stats
       (fight false 50 500)))

(defn part-2
  "Day 22 Part 2"
  [input]
  (->> input
       boss-stats
       (fight true 50 500)))
