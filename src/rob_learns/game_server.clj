(ns rob-learns.game-server
  (:use clojure.pprint)
  (:require [rob-learns.helpers :refer :all]
            [rob-learns.core :refer :all]
            [failjure.core :as f]))


(def player_1 (atom nil))
(def player_2 (atom nil))

(defn get-atom
  [name]
  (cond (= name :player1  ) player_1
        (= name :player2  ) player_2))


(def directionfn {:h get-coordinates-h :v get-coordinates-v})


(defn set-game-state
  [player inventory fleet status board]
  (reset! player {:board board :inventory inventory :fleet fleet :status status}))


(defn initialize-game
  []
  (set-game-state player_1  (fleet) (fleet) nil (make-board))
  (set-game-state player_2  (fleet) (fleet) nil (make-board))
  (make-board))


(defn place-move
  [state ship direction x y]
  (let [result
        (f/ok->> ship
                 ( place-ship direction  (:fleet @state) (:inventory @state) (:board @state) x y)
                 ( set-game-state state (dissoc (:inventory @state) (keyword ship)) (:fleet @state)
                            {:success"Placed a piece."}))]

    (if (f/failed? result) (swap! state assoc :status {:error result}) result)))


(defn attack-move
  [attacker defender x y]
  (let [res (attack-defender @attacker @defender [y x])]
    (swap! attacker assoc :status (:status res))
    (reset! defender res)))


;
;

;(initialize-game)
;(place-move player_1 "destroyer" (:h directionfn) 0 1)
;(place-move player_1 "submarine"  (:h directionfn) 2 1)
;(place-move player_1 "carrier"  (:v directionfn)  0 2)
;(place-move player_1 "cruiser"  (:v directionfn)  3 2)
;(place-move player_1 "battleship"  (:h directionfn) 0 9)
;
;
;
;;(attack-move player_2 player_1 2 1)
;(attack-move player_2 player_1 1 1)
;(attack-move player_2 player_1 0 1)
;(attack-move player_2 player_1 0 1)
;(pprint @player_1)
;;(pprint @player_2)
;(attack-move player_2 player_1 9 1)
;(pprint @player_1)



