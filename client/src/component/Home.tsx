import axios from "axios";
import SocketClient from "../sockjs/SocketClient";
import { useEffect } from "react";
import data from "../resource/secret/server-spec.json"
import { RoomCreationRequest } from "../type/request/requestType";
import { RoomInfoResponse } from "../type/response/responseType";

const hostName = "test-host";
const hostId = 14;

export default function Home() {
    const sockClient = SocketClient.getInstance();
    useEffect(() => {
        test();    
    }, []);

    return <div></div>;
}

async function test() {
    const roomCreationInfo: RoomCreationRequest = {
        gameName: "First",
        hostId: hostId,
        hostName: hostName
    };
    const roomInfo = await axios.post(`${data.restApiUrl}/game/room`, roomCreationInfo) as RoomInfoResponse;
    console.log(JSON.stringify(roomInfo, null, 4));
}