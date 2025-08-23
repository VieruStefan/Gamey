db = db.getSiblingDB('gamey');
db.createUser({
    user: "user",
    pwd: "user",
    roles: [
        { role: "read", db: "gamey" }
    ]
});
db.createCollection('products');