import { Link } from "react-router-dom";
import BasketItem from "./BasketItem";
import { Button } from "@mui/material";

function Basket(props){
    console.log(props.restaurant);

    function DisplayItems(props2){
        var hasAmount = false;
        props2.items.forEach((item) => {
            if(item.amount > 0){
                hasAmount = true;
            }
        });

        if(props2.items === null || hasAmount === false){
            return(
                <h3>Fill your basket</h3>
            );
        } else{
            var totalPriceOrder = 0;
            props2.items.forEach((item) => {
                totalPriceOrder += item.totalPrice;
            });

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
                    {/* <Button LinkComponent={Link} to={"/checkout"} state={{items: props2.items, restaurant: props.restaurant}} className="basketButton">
                        Checkout
                    </Button> */}
                    <Link to={"/checkout"} state={{items: props2.items, restaurant: props.restaurant}} className="basketButtonLink" >
                        <button className="basketButton">Checkout</button>
                    </Link>
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

export default Basket;