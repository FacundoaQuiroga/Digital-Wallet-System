import { useState } from 'react'

function TransferForm({ onTransfer }) {
  const [receiverWalletId, setReceiverWalletId] = useState('')
  const [amount, setAmount] = useState('')

  function handleSubmit(event) {
    event.preventDefault()

    if (!receiverWalletId || !amount) {
      return
    }

    onTransfer(Number(receiverWalletId), Number(amount))

    setReceiverWalletId('')
    setAmount('')
  }

  return (
    <form onSubmit={handleSubmit}>
      <h2>Transfer money</h2>

      <input
        type="number"
        placeholder="Receiver wallet id"
        value={receiverWalletId}
        onChange={(event) => setReceiverWalletId(event.target.value)}
      />

      <input
        type="number"
        placeholder="Amount"
        value={amount}
        onChange={(event) => setAmount(event.target.value)}
      />

      <button type="submit">Transfer</button>
    </form>
  )
}

export default TransferForm