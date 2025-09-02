db = db.getSiblingDB('gamey');
db.createUser({
    user: "user",
    pwd: "user",
    roles: [
        { role: "read", db: "gamey" }
    ]
});
db.createUser({
    user: "admin",
    pwd: "admin",
    roles: [
        { role: "readWrite", db: "gamey" }
    ]
});
db.createCollection('products');
db.createCollection('jobs');
db.products.createIndex({ "url": 1, "jobId": 1 }, { unique: true })