package org.sid.web.controller;


import org.sid.dao.PaiementDao;
import org.sid.model.Paiement;
import org.sid.web.exceptions.PaiementExistantException;
import org.sid.web.exceptions.PaiementImpossibleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaiementController {

    @Autowired
    PaiementDao paiementDao;

    @PostMapping(value = "/paiement")
    public ResponseEntity<Paiement> payerUneCommande(@RequestBody Paiement paiement) {


        //Vérifions s'il y a déjà un paiement enregistré pour cette commande
        Paiement paiementExistant = paiementDao.findByidCommande(paiement.getIdCommande());
        if (paiementExistant != null) throw new PaiementExistantException("Cette commande est déjà payée");

        //Enregistrer le paiement
        Paiement nouveauPaiement = paiementDao.save(paiement);


        if (nouveauPaiement == null)
            throw new PaiementImpossibleException("Erreur, impossible d'établir le paiement, réessayez plus tard");


        //TODO Nous allons appeler le Microservice Commandes ici pour lui signifier que le paiement est accepté

        return new ResponseEntity<Paiement>(nouveauPaiement, HttpStatus.CREATED);

    }


}
