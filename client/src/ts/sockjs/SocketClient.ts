import serverSpecResource from "../../resource/secret/server-spec.json";
import Stomp from "stompjs";
import SockJS from "sockjs-client";

export default class SocketClient {
    private static singleton: SocketClient;
    private stompClient: Stomp.Client | undefined;

    private constructor() {}

    private async init() {
        let socket = new SockJS(serverSpecResource.socketUrl);
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, this.onConnected, this.onError);
    }

    private onConnected(): void {
        console.log("Succeeded to connect");
    }

    private onError(err: any): void {
        console.error(err);
    }

    public static async getInstance(): Promise<SocketClient> {
        if (!this.singleton) {
            this.singleton = new SocketClient();
            await this.singleton.init();
        }
        return this.singleton;
    }

    public sendMessage(endpoint: string, headers: object, data: object) {
        setTimeout(async () => {
            await this.stompClient?.send(
                endpoint,
                headers,
                JSON.stringify(data)
            );
        }, 100);
    }

    public async subscribe(
        url: string,
        callback: (payload: any) => void,
        headers?: {}
    ): Promise<Stomp.Subscription> {
        return new Promise((resolve, reject) => {
            setTimeout(async () => {
                const subscription = await this.stompClient!.subscribe(
                    url,
                    callback,
                    headers
                );
                resolve(subscription);
            }, 500);
        });
    }

    public unsubscribe(subscription: Stomp.Subscription): void {
        subscription.unsubscribe();
    }
}
