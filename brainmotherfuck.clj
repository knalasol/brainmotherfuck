(ns brainmf)

(def emptystate {:code ""
                 :reader 0
                 :position [0 0]
                 :field {}
                 :env {}
                 :whilestack []
                 :func? false
                 :func-buffer ""
                 :input '()
                 :output ""})

(defn brainmother 
  ([codestring] 
    (brainmother codestring ""))
  ([codestring input]
    (let [state (atom {:code ""
                       :reader 0
                       :position [0 0]
                       :field {}
                       :env {}
                       :whilestack []
                       :func? false
                       :func-buffer ""
                       :input '()
                       :output ""})]
      (swap! state assoc :code codestring)
      (swap! state assoc :input (seq input))
      (while (< (:reader @state) (count (:code @state))) (next! state))
      (let [output (:output @state)] (when (seq output) (println output))))))

(defn next! 
  [state]
  (do (swap! state command (nth (:code @state) (:reader @state)))
      (swap! state update :reader inc)))

(defmulti command (fn [state character] (identity character)))

(defn update-pair 
  [p first? f]
  (if first?
    [(f (first p)) (second p)]
    [(first p) (f (second p))]))

(defn match-paren 
  [left right string current]
  (let [brackets (atom 1 :validator #(not (neg? %)))
        newposition (atom current :validator #(< % (count string)))]
        (while (pos? @brackets) 
          (do (swap! newposition inc)
              (condp = (nth string @newposition)
                  left (swap! brackets inc)
                  right (swap! brackets dec)
                  (do))))
        (deref newposition)))

(defmethod command \+ 
  [{:keys [position] :as state} _]
  (update-in state (into [:field] position) (fnil inc 0)))

(defmethod command \- 
  [{:keys [position] :as state} _]
  (update-in state (into [:field] position) (fnil dec 0)))

(defmethod command \>
  [state _]
  (update state :position update-pair true inc))

(defmethod command \<
  [state _]
  (update state :position update-pair true dec))

(defmethod command \^
  [state _]
  (update state :position update-pair false inc))

(defmethod command \v
  [state _]
  (update state :position update-pair false dec))

(defmethod command \[
  [{:keys [code reader position field] :as state} _]
    (if (zero? (get-in field position 0))
      (assoc state :reader (match-paren \[ \] code reader))
      (update state :whilestack conj reader)))

(defmethod command \]
  [{:keys [whilestack] :as state} _]
  (-> state
    (assoc :reader (dec (peek whilestack)))
    (update :whilestack pop)))

(defmethod command \.
  [{:keys [position field] :as state} _]
  (update state :output str (char (get-in field position 0))))

(defmethod command \,
  [{:keys [position field input] :as state} _]
  (-> state
      (update :field assoc-in position ((fnil int 0) (first input)))
      (update :input next)))

(defmethod command \(
  [{:keys [func?] :as state} _]
    (assoc state :func? true))

(defmethod command \)
  [{:keys [reader env func? func-buffer] :as state} _]
  (-> state
    (assoc :func? false)
    (update :code #(str (subs % 0 reader) ((keyword func-buffer) env) (subs % reader)))
    (assoc :func-buffer "")))
  
(defmethod command \:
  [{:keys [code reader env func-buffer] :as state} _]
    (let [enddef (match-paren \( \) code reader)]
      (-> state
          (assoc :func? false)
          (update :env assoc (keyword func-buffer) (subs code (inc reader) enddef))
          (assoc :func-buffer "")
          (assoc :reader enddef))))
  
(defmethod command \#
  [{:keys [position field] :as state} _]
  (do (println position (get-in field position 0))
      state))
  
(defmethod command :default
  [{:keys [func?] :as state} character]
  (if func? 
    (update state :func-buffer #(str % character))
    state))
