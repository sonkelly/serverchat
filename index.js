const io = require('socket.io')(3030);
// mang user online
const arrUer = [];
// khi co ket noi tao ra obj socket
io.on('connection', socket => {
    socket.on('NGUOI_DUNG_KET_NOI', user => {
        arrUer.push(user);
        socket.userId = user.userId;
        console.log("SV Ten: "+user.userName);
        console.log("SV MSV: "+user.userId);
        socket.emit('DANH_SACH_ONLINE', arrUer);
        socket.broadcast.emit('CO_NGUOI_DUNG_MOI', user);
    });
    socket.on('GUI_TIN_NHAN', message => {
        socket.broadcast.emit('CO_TIN_NHAN_MOI', message);
    });
    socket.on('disconnect', () => {
        const index = arrUer.findIndex(user => user.userId === socket.userId);
        arrUer.splice(index, 1);
        io.emit('NGAT_KET_NOI', socket.userId);
    });
});
