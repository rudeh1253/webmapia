import data from "../../secret/server-spec.json";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default class SocketClient {
    private static singleton: SocketClient;
    private stompClient: Stomp.Client;

    constructor() {
        let socket = new SockJS(data.socketUrl);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, this.onConnected, this.onError);
    }

    private onConnected(): void {
        setTimeout(() => {
            this.stompClient.subscribe(data.socketEndpoints.notificationPublic);
        }, 500);
    }

    private onError(err: any): void {
        console.error(err);
    }

    public static getInstance(): SocketClient {
        if (this.singleton === null) {
            this.singleton = new SocketClient();
        }
        return this.singleton;
    }

    public async sendMessage(endpoint: string, headers: object, data: object) {
        await this.stompClient.send(endpoint, headers, JSON.stringify(data));
    }
}
