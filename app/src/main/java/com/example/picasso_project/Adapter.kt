package com.example.picasso_project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.picasso_project.databinding.OneCharacterTemplateBinding
import com.squareup.picasso.Picasso

class Adapter: RecyclerView.Adapter<Adapter.CharacterHolder>() {
    private var charactersList =  ArrayList<Character>()
    class CharacterHolder(item: View): RecyclerView.ViewHolder(item) {
        val binding = OneCharacterTemplateBinding.bind(item)
        fun forBind(characterData: Character) = with(binding) {
            Picasso.get().load(characterData.image).error(R.drawable.cross_error).into(itemImage)
            itemText.text = characterData.characterName
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.one_character_template, parent, false)
        return CharacterHolder(view)
    }

    override fun getItemCount(): Int {
        return charactersList.size
    }

    override fun onBindViewHolder(holder: CharacterHolder, position: Int) {
        holder.forBind(charactersList[position])
    }

    fun addCharacter(character_lst: MutableList<Character>) {
        val uniqueCharacters = character_lst.filter { newCharacter ->
            charactersList.none { existingCharacter ->
                existingCharacter.characterName == newCharacter.characterName
            }
        }
        charactersList.addAll(uniqueCharacters)
        notifyDataSetChanged()
    }
}