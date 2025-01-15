# SeaGuard

Di seguito sono riportate le funzionalità fino ad ora implementate in questa versione del codice e quella ancora da sviluppare: 

[N.B. per testare il codice e effettuare il login le credenziali sono: 
email:      info@seaguard.com
password:   testSeaGuardAuth.2024]


FUNZIONALITA’ IMPLEMENTATE

Backend:

- Database: Configurazione di Firebase completata

- Realtime Database e Firestore: Utilizzati per la gestione delle segnalazioni e dei profili utenti.

- Autenticazione Firebase: Implementata l'autenticazione degli utenti tramite email e password.

- Firebase Storage: Attivato per il caricamento e la gestione delle immagini delle segnalazioni.


Frontend:

- Login

- Registrazione

- Homepage: implementazione della mappa interattiva con button per aggiungere segnalazione

- Add Report: pagina per la creazione di una segnalazione con posizione, data, descrizione, categoria, foto, livello di pericolo.

- Segnalazioni: schermata per visualizzazione minimale delle segnalazioni presenti. (TODO visualizzazione più approfondita delle segnalazioni e ordinamento per data)

- Scopri: è presente il Fragment ma la sezione è ancora da implementare

- Impostazioni: visualizza i dati dell'utente con possibilità di modificare il profilo (TODO sezione notifiche e informazioni)

Integrazione: 

- Collegamento iniziale tra il frontend e il backend realizzato: I dati delle segnalazioni vengono salvati e recuperati dal database Firebase. Le immagini vengono caricate e archiviate correttamente su Firebase Storage.

