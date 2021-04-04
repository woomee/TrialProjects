import * as functions from "firebase-functions";
import * as admin from "firebase-admin";

admin.initializeApp();

// // Start writing Firebase Functions
// // https://firebase.google.com/docs/functions/typescript
//
export const writeToFirestore = functions.https.onRequest(async (request, response) => {
    functions.logger.info("Hello logs!", { structuredData: true });

    /* Firestoreに500レコード書いてみる */
    let loopNum = 500;
    for (let index = 0; index < loopNum; index++) {
        await admin.firestore().collection("TCO-TOOL/tco/writeTest").add({ data: "writeTest: " + index });
        if (index % 10 == 0) {
            functions.logger.info("Write: " + index);
        }
    }

    response.send("Hello from Firebase!");
});

export const writeToFirestoreAsync = functions.https.onRequest((request, response) => {
    functions.logger.info("Hello logs!", { structuredData: true });

    /* Firestoreに500レコード書いてみる */
    let loopNum = 500;
    for (let index = 0; index < loopNum; index++) {
        admin.firestore().collection("TCO-TOOL/tco/writeTest").add({ data: "writeTest: " + index });
        if (index % 10 == 0) {
            functions.logger.info("Write: " + index);
        }
    }

    response.send("Hello from Firebase!");
});

export const writeToFirestoreBatch = functions.https.onRequest((request, response) => {
    functions.logger.info("Hello logs!", { structuredData: true });

    var batch = admin.firestore().batch();

    /* Firestoreに500レコード書いてみる */
    let loopNum = 500;
    for (let index = 0; index < loopNum; index++) {
        var docRef = admin.firestore().collection("TCO-TOOL/tco/writeTest").doc();
        batch.set(docRef, { data: "writeTest: " + index });
        // admin.firestore().collection("TCO-TOOL/tco/writeTest").add({ data: "writeTest: " + index });
        if (index % 10 == 0) {
            functions.logger.info("Write: " + index);
        }
    }
    batch.commit().then(() => {
        response.send("Hello from Firebase!");
    });
});
