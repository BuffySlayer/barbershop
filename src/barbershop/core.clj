(ns barbershop.core)
(def empty-chairs (ref 3))
(def barber (agent 0))

(defn debug [msg customer]
  "Print debug statement with message, msg, for customer, customer."
  (println msg (apply str (repeat (- 40 (count msg)) \space)) customer)
  (flush))

(defn cut-hair [tally customer]
  "Cut customer's hair."
  (dosync (commute empty-chairs inc))
  (debug "(b) cutting hair of customer" customer)
  (Thread/sleep (+ 100 (rand-int 600)))
  (debug "(b) done cutting hair of customer" customer)
  (inc tally))

(defn enter-barbershop [customer]
  "Customer enters barbershop."
  (debug "(c) customer enters barbershop" customer)
  (when-not (dosync
		(when (pos? @empty-chairs)
		  (alter empty-chairs dec)
		  (send-off barber cut-hair customer)))
    (debug "(s) turning away customer" customer)))

(doseq [customer (range 1 20)]
  (Thread/sleep (+ 100 (rand-int 200)))
  (future (enter-barbershop customer)))

(Thread/sleep 2000)
(println "Customers that received haircuts today:" @barber)

