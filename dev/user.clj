(ns user
  (:require [xtdb.api :as xt]))

(comment 
  
;; (def node (xt/start-node {}))  

(def node (xt/start-node {:xtdb.jdbc/connection-pool {:dialect {:xtdb/module 'xtdb.jdbc.psql/->dialect}
                                                      :db-spec {:host "localhost"
                                                                :dbname "xtdb"
                                                                :user "postgres"
                                                                :password "postgres"}}
                          :xtdb/tx-log {:xtdb/module 'xtdb.jdbc/->tx-log
                                        :connection-pool :xtdb.jdbc/connection-pool}
                          :xtdb/document-store {:xtdb/module 'xtdb.jdbc/->document-store
                                                :connection-pool :xtdb.jdbc/connection-pool}}))


(def bmw {:xt/id :bmw
          :make/name "BMW"
          :make/country "Germany"})

(def opel {:xt/id :opel
           :make/name "Opel"
           :make/country "Germany"})

(def car1 {:xt/id :car1
           :car/year 2015
           :car/make :bmw
           :car/model "3 Series"
           :car/variant "135i"})

(def car2 {:xt/id :car2
           :car/year 2013
           :car/make :opel
           :car/model "Corsa"
           :car/variant "Cosmo"})

(def coisas (mapv (fn [c] [::xt/put c]) [bmw opel car1 car2]))


coisas

(xt/submit-tx node coisas)

(xt/q
 (xt/db node)
 '{:find [(pull ?car [* {:car/make [*]}])]
   :where [[?make :make/name "BMW"]
           [?car :car/make ?make]]})

;; tras no map a parte do join com make
;; o que desenha o um grafo.
(xt/q
 (xt/db node)
 '{:find [(pull ?car [* {:car/make [*]}])]
   :where [[?make :make/name "BMW"]
           [?car :car/make ?make]]})

;; retorna o proprio id
(xt/q
 (xt/db node)
 '{:find [?make]
   :where [[?make :xt/id :bmw]]})

;; exemplo bobo de retornar tudo.
(xt/q
 (xt/db node)
 '{:find [(pull ?make [*])]
    :where [[?make :xt/id :bmw]
            [?make :make/country ?country]]})

)