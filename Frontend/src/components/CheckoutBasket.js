import BasketItem from "./BasketItem";

function CheckoutBasket(props) {

    function DisplayItems(props2){
        var hasAmount = false;
        props2.items.forEach((item) => {
            if(item.amount > 0){
                hasAmount = true;
            }
        });

        if(props2.items === null || hasAmount === false){
            sessionStorage.removeItem("itemsSelected");

            return(
                <h3>Fill your basket</h3>
            );
        } else{
            var totalPriceOrder = 0;
            props2.items.forEach((item) => {
                totalPriceOrder += item.totalPrice;
            });

            sessionStorage.setItem("itemsSelected", true);

            return(
                <>
                    <ul className="basketItemList">
                        {props2.items.map((item) => (
                            <BasketItem key={item.id} item={item} />
                        ))}
                    </ul>
                    <div className="basketTotal">
                        <p className="basketItemName"> Total </p> 
                        <p className="basketItemPrice"> € {totalPriceOrder} </p>
                    </div>
                </>
            );
        }
    }

    return(
        <>
            <h2>Basket</h2>
            <DisplayItems items={props.items} />
        </>
    );
}

export default CheckoutBasket;